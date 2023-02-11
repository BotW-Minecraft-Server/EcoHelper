package link.botwmcs.samchai.ecohelper.util;

import committee.nova.lighteco.util.EcoUtils;
import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import net.minecraft.world.entity.player.Player;

import java.math.BigDecimal;
import java.util.Optional;

public class BalanceUtil {
    public static double getBalance(Player player) {
        Optional<BigDecimal> balance = EcoUtils.getBalance(player);
        return balance.map(BigDecimal::doubleValue).orElse(0.0);
    }

    public static EcoUtils.EcoActionResult addBalance(Player player, double value) {
        value = roundDouble(value);
        return EcoUtils.debt(player, BigDecimal.valueOf(value));
    }

    public static EcoUtils.EcoActionResult removeBalance(Player player, double value) {
        value = roundDouble(value);
        return EcoUtils.credit(player, BigDecimal.valueOf(value));
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

    public static double roundDouble(double value) {
        return Math.round(value * Math.pow(10, EcoHelperConfig.CONFIG.decimal_place.get())) / Math.pow(10, EcoHelperConfig.CONFIG.decimal_place.get());
    }


}
