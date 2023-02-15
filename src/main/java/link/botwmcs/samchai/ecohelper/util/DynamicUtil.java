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

    public static Item getBalanceUnitItem() {
        try {
            String itemName = EcoHelperConfig.CONFIG.default_balance_unit.get();
            ResourceLocation itemRL = new ResourceLocation(itemName);
            return ForgeRegistries.ITEMS.getValue(itemRL);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Getting default balance unit item (Please check your config)");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Command to be executed");
            crashreportcategory.setDetail("Item", EcoHelperConfig.CONFIG.default_balance_unit::get);
            throw new ReportedException(crashreport);
        }
    }

    public static ItemStack getBalanceUnitItemStack() {
        Item item = getBalanceUnitItem();
        return new ItemStack(item);
    }

    public static void setBalanceUnitItemWorth(double worth) {
        EcoHelperConfig.CONFIG.default_balance_unit_worth.set(worth);
    }

    public static double getDynamicSellingPrice(double currentPrice, double playerBalance) {
        return priceDownByProperty(currentPrice, playerBalance, EcoHelperConfig.CONFIG.dynamic_economic_basic_property.get());
    }

}
