package link.botwmcs.samchai.ecohelper.util;

import committee.nova.lighteco.capabilities.impl.Account;
import committee.nova.lighteco.util.EcoUtils;
import link.botwmcs.samchai.ecohelper.EcoHelper;
import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.math.BigDecimal;
import java.util.Optional;
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
//        return EcoUtils.debt(player, BigDecimal.valueOf(value));
        return EcoUtils.vary(player, BigDecimal.valueOf(value), RANGES, (p, v) -> v, RANGES);
    }

    public static EcoUtils.EcoActionResult removeBalance(Player player, double value) {
        value = roundDouble(value);
//        return EcoUtils.credit(player, BigDecimal.valueOf(value));
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
        int remove = ContainerHelper.clearOrCountMatchingItems(inventory, itemStack -> itemStack.is(Items.EMERALD), count, true);
        if (remove >= count) {
            ContainerHelper.clearOrCountMatchingItems(inventory, itemStack -> itemStack.is(Items.EMERALD), count, false);
            double amountPerItem = EcoHelperConfig.CONFIG.default_balance_unit_worth.get();
            double amountTotal = amountPerItem * count;
            EcoUtils.EcoActionResult result = addBalance(player, amountTotal);
            if (result != EcoUtils.EcoActionResult.SUCCESS) {
                inventory.add(new ItemStack(Items.EMERALD, count));
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean exchangeToBalance(Player player) {
        int total = 0;
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.getItem().equals(Items.EMERALD)) {
                total += stack.getCount();
            }
        }
        if (total == 0) {
            return false;
        } else {
            return exchangeToBalance(player, total);
        }
    }


    public static double roundDouble(double value) {
        return Math.round(value * Math.pow(10, EcoHelperConfig.CONFIG.decimal_place.get())) / Math.pow(10, EcoHelperConfig.CONFIG.decimal_place.get());
    }



}
