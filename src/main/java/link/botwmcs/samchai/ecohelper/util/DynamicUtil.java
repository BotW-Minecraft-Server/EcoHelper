package link.botwmcs.samchai.ecohelper.util;

import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class DynamicUtil {
    // Basic by https://github.com/Tining123/DemonMarket/blob/main/src/main/java/com/tining/demonmarket/common/util/MathUtil.java
    public static double priceDownByProperty(double price, double property, double basicProperty){
        // 0.5 is squareroot
        return ((price) / Math.pow(Math.exp((property / basicProperty)), 0.5) + (price / (1 + property / basicProperty))) / 2;
    }

    public static double getDynamicSellingPrice(double currentPrice, double playerBalance) {
        double worth = priceDownByProperty(currentPrice, playerBalance, EcoHelperConfig.CONFIG.dynamic_economic_basic_property.get());
        return BalanceUtil.roundDouble(worth);
    }

}
