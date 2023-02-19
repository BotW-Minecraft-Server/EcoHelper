package link.botwmcs.samchai.ecohelper.util;

import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class DynamicUtil {
    // Basic by https://github.com/Tining123/DemonMarket/blob/main/src/main/java/com/tining/demonmarket/common/util/MathUtil.java
    public static double priceDownByProperty(double price, double property, double basicProperty){
        // 0.5 is squareroot
        return ((price) / Math.pow(Math.exp((property / basicProperty)), 0.5) + (price / (1 + property / basicProperty))) / 2;
    }

//    private static double getDynamicSellingPrice(double currentPrice, double playerBalance) {
//        if (EcoHelperConfig.CONFIG.dynamic_economic.get()) {
//            double worth = priceDownByProperty(currentPrice, playerBalance, EcoHelperConfig.CONFIG.dynamic_economic_basic_property.get());
//            return BalanceUtil.roundDouble(worth - EcoHelperConfig.CONFIG.tax_baseline.get());
//        } else {
//            return currentPrice;
//        }
//    }

    public static double getDynamicSellingPrice(double currentPrice, ServerPlayer player) {
        if (EcoHelperConfig.CONFIG.dynamic_economic.get()) {
            if (EcoHelperConfig.CONFIG.global_statistics.get()) {
                double globalBalance = BalanceUtil.getAllBalance(player.getServer());
                double worth = priceDownByProperty(currentPrice, globalBalance, EcoHelperConfig.CONFIG.dynamic_economic_basic_property.get());
                return BalanceUtil.roundDouble(worth - EcoHelperConfig.CONFIG.tax_baseline.get());
            } else {
                double localBalance = BalanceUtil.getBalance(player) + currentPrice;
                double worth = priceDownByProperty(currentPrice, localBalance, EcoHelperConfig.CONFIG.dynamic_economic_basic_property.get());
                return BalanceUtil.roundDouble(worth - EcoHelperConfig.CONFIG.tax_baseline.get());
            }
        } else {
            return currentPrice;
        }
    }
}
