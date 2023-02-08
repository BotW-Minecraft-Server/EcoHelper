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
    public final ForgeConfigSpec.BooleanValue dynamic_economic;
    public final ForgeConfigSpec.ConfigValue<String> economy_mode;
    public final ForgeConfigSpec.ConfigValue<String> bukkit_economy_system;
    public final ForgeConfigSpec.ConfigValue<String> get_bukkit_economy_command;
    public final ForgeConfigSpec.ConfigValue<Integer> default_balance;
    public final ForgeConfigSpec.ConfigValue<Integer> min_balance;
    public final ForgeConfigSpec.ConfigValue<Integer> max_balance;
    public final ForgeConfigSpec.ConfigValue<String> default_balance_unit;

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
        dynamic_economic = builder
                .comment("Enable dynamic economic system")
                .define("dynamic_economic", true);
        economy_mode = builder
                .comment("Economy mode: initiative, passive")
                .define("economy_mode", "passive");
        bukkit_economy_system = builder
                .comment("Bukkit economy system (If economy_mode is passive): Essentials, CMI, Vault")
                .define("bukkit_economy_system", "Vault");
        get_bukkit_economy_command = builder
                .comment("Get bukkit economy command (If economy_mode is passive, %s is player name)")
                .define("get_bukkit_economy_command", "/papi parse %s %vault_eco_balance%");
        default_balance = builder
                .comment("Default balance")
                .define("default_balance", 0);
        min_balance = builder
                .comment("Min balance")
                .define("min_balance", 0);
        max_balance = builder
                .comment("Max balance")
                .define("max_balance", 1000000);
        default_balance_unit = builder
                .comment("Default balance unit (Bind an item to exchange for balance, set minecraft:air to disable)")
                .define("default_balance_unit", "minecraft:emerald");

    }


}
