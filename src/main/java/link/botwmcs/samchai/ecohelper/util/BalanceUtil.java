package link.botwmcs.samchai.ecohelper.util;

import committee.nova.lighteco.util.EcoUtils;
import link.botwmcs.samchai.ecohelper.EcoHelper;
import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import link.botwmcs.samchai.ecohelper.recipe.TradableItemRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;


public class BalanceUtil {
    // max & min
    public static final BiPredicate<Player, BigDecimal> RANGES = (p, v) ->
            v.compareTo(BigDecimal.valueOf(EcoHelperConfig.CONFIG.max_balance.get())) < 1 && v.compareTo(BigDecimal.valueOf(EcoHelperConfig.CONFIG.min_balance.get())) > -1;

    public static double getBalance(Player player) {
        Optional<BigDecimal> balance = EcoUtils.getBalance(player);
        return balance.map(BigDecimal::doubleValue).orElse(0.0);
    }


    public static EcoUtils.EcoActionResult addBalance(Player player, double value) {
        value = roundDouble(value);
        return EcoUtils.vary(player, BigDecimal.valueOf(value), RANGES, (p, v) -> v, RANGES);
    }

    public static EcoUtils.EcoActionResult removeBalance(Player player, double value) {
        value = roundDouble(value);
        return EcoUtils.vary(player, BigDecimal.valueOf(value), RANGES, (p, v) -> v.negate(), RANGES);
    }

    public static EcoUtils.EcoActionResult setBalance(Player player, double value) {
        value = roundDouble(value);
        EcoUtils.EcoActionResult result = removeBalance(player, getBalance(player));
        if (result != EcoUtils.EcoActionResult.SUCCESS) return result;
        return addBalance(player, value);
    }

    public static EcoUtils.EcoActionResult transferBalance(Player from, Player to, double value) {
        value = roundDouble(value);
        EcoUtils.EcoActionResult result = removeBalance(from, value);
        if (result != EcoUtils.EcoActionResult.SUCCESS) return result;
        return addBalance(to, value);
    }

    // Basic by https://gist.github.com/Sam-Chai/70efee8f15d171b358bb1ae2a444cbd9
    public static boolean exchangeToBalance(Player player, int count) {
        Inventory inventory = player.getInventory();
        AtomicReference<EcoUtils.EcoActionResult> result = new AtomicReference<>(EcoUtils.EcoActionResult.SUCCESS);
        int remove = ContainerHelper.clearOrCountMatchingItems(inventory, itemStack -> {
            Level level = player.level;
            TradableItemRecipe recipe = TradableItemRecipe.getRecipeFromItem(level, itemStack);
            return recipe != null;
        }, count, true);
        if (remove >= count) {
            ContainerHelper.clearOrCountMatchingItems(inventory, itemStack -> {
                Level level = player.level;
                TradableItemRecipe recipe = TradableItemRecipe.getRecipeFromItem(level, itemStack);
                if (recipe != null) {
                    double worth = recipe.getWorth() * count;
                    result.set(addBalance(player, worth));
                    if (result.get() != EcoUtils.EcoActionResult.SUCCESS) {
                        inventory.add(itemStack);
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }, count, false);
        } else {
            result.set(EcoUtils.EcoActionResult.ARG_ILLEGAL);
        }
        return result.get() == EcoUtils.EcoActionResult.SUCCESS;
    }

    public static boolean exchangeToBalance(Player player) {
        Inventory inventory = player.getInventory();
        int total = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            TradableItemRecipe recipe = TradableItemRecipe.getRecipeFromItem(player.level, stack);
            if (recipe != null) {
                total += stack.getCount();
            }
        }
        return exchangeToBalance(player, total);
    }

    public static double getTradableItemWorth(Level level, ItemStack itemStack) {
        double worth = 0;
        TradableItemRecipe recipe = TradableItemRecipe.getRecipeFromItem(level, itemStack);
        if (recipe != null) {
            worth = recipe.getWorth() * itemStack.getCount();
        }
        return worth;
    }


    public static double roundDouble(double value) {
        return Math.round(value * Math.pow(10, EcoHelperConfig.CONFIG.decimal_place.get())) / Math.pow(10, EcoHelperConfig.CONFIG.decimal_place.get());
    }



}
