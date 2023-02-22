package link.botwmcs.samchai.ecohelper.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber
public class EcoHelperConfig {
    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CONFIG_SPEC);
    }
    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final EcoHelperConfig CONFIG;

    public final ForgeConfigSpec.BooleanValue enabled;
    public final ForgeConfigSpec.BooleanValue enable_gui;
    public final ForgeConfigSpec.BooleanValue enable_item_tooltip;
    public final ForgeConfigSpec.ConfigValue<Double> default_balance;
    public final ForgeConfigSpec.ConfigValue<Double> min_balance;
    public final ForgeConfigSpec.ConfigValue<Double> max_balance;
    public final ForgeConfigSpec.ConfigValue<String> default_balance_unit;
    public final ForgeConfigSpec.ConfigValue<String> balance_alias;
    public final ForgeConfigSpec.ConfigValue<Double> default_balance_unit_worth;
    public final ForgeConfigSpec.ConfigValue<Integer> decimal_place;

    public final ForgeConfigSpec.BooleanValue dynamic_economic;
    public final ForgeConfigSpec.ConfigValue<Double> dynamic_economic_basic_property;
    public final ForgeConfigSpec.ConfigValue<Double> tax_baseline;
    public final ForgeConfigSpec.BooleanValue global_statistics;

    public final ForgeConfigSpec.BooleanValue enable_sql;
    public final ForgeConfigSpec.ConfigValue<String> sql_type;

    public final ForgeConfigSpec.ConfigValue<String> economy_sync_mode;
    public final ForgeConfigSpec.ConfigValue<String> bukkit_economy_system;


    static {
        Pair<EcoHelperConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(EcoHelperConfig::new);
        CONFIG_SPEC = specPair.getRight();
        CONFIG = specPair.getLeft();
    }

    EcoHelperConfig(ForgeConfigSpec.Builder builder) {
        builder.push("general");
//        builder.comment("General settings for EcoHelper");
//        builder.comment("If have any problem, please look at: https://github.com/Sam-Chai/EcoHelper");
        enabled = builder
                .comment("Enable EcoHelper")
                .define("enabled", true);
        enable_gui = builder
                .comment("Enable player's balance GUI")
                .define("enable_gui", true);
        enable_item_tooltip = builder
                .comment("Enable tradable item worth tooltip display")
                .define("enable_item_tooltip", true);
        default_balance = builder
                .comment("Default balance")
                .define("default_balance", 0.00);
        min_balance = builder
                .comment("Min balance")
                .define("min_balance", 0.00);
        max_balance = builder
                .comment("Max balance")
                .define("max_balance", 1000000.00);
        default_balance_unit = builder
                .comment("Default balance unit (Bind an item to exchange for balance, set minecraft:air to disable)")
                .define("default_balance_unit", "minecraft:emerald");
        balance_alias = builder
                .comment("Balance alias (Default $, please use UTF-8 code, set empty to disable)")
                .define("balance_alias", "$");
        default_balance_unit_worth = builder
                .comment("Default balance unit worth (Default 10.0)")
                .define("default_balance_unit_worth", 10.00);
        decimal_place = builder
                .comment("Decimal place (Default 2, like 5.29)")
                .define("decimal_place", 2);
        builder.pop();

        builder.push("dynamic_economic");
        dynamic_economic = builder
                .comment("Enable dynamic economic system")
                .define("dynamic_economic", true);
        dynamic_economic_basic_property = builder
                .comment("The average balance of single player in mathematical expectation")
                .define("dynamic_economic_basic_property", 500000.0);
        tax_baseline = builder
                .comment("The baseline of tax (Although player's balance is 0, he still need to pay the baseline tax. Default 5%)")
                .define("tax_baseline", 0.05);
        global_statistics = builder
                .comment("Global / Local statistics (Global statistics will calculate all players' balance, it may cause some inflation problem. Default false)")
                .define("global_statistics", false);
        builder.pop();

        builder.push("sql_settings");
        enable_sql = builder
                .comment("Enable SQL")
                .define("enable_sql", false);
        sql_type = builder
                .comment("SQL type (sqlite)")
                .define("sql_type", "sqlite");
        builder.pop();

        builder.push("impl_settings");
        economy_sync_mode = builder
                .comment("Economy sync mode (initiative, passive, set false to disable sync)")
                .define("economy_mode", "false");
        bukkit_economy_system = builder
                .comment("Bukkit economy system (If economy_mode is passive): Essentials, CMI")
                .define("bukkit_economy_system", "CMI");
        builder.pop();



    }


}
