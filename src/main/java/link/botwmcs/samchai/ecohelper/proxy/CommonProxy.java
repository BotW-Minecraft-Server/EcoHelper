package link.botwmcs.samchai.ecohelper.proxy;

import committee.nova.lighteco.util.EcoUtils;
import link.botwmcs.samchai.ecohelper.EcoHelper;
import link.botwmcs.samchai.ecohelper.command.EcoCommand;
import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import link.botwmcs.samchai.ecohelper.network.ModNetwork;
import link.botwmcs.samchai.ecohelper.recipe.ModRecipes;
import link.botwmcs.samchai.ecohelper.recipe.TradableItemRecipe;
import link.botwmcs.samchai.ecohelper.util.BalanceUtil;
import link.botwmcs.samchai.ecohelper.util.PlayerUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = EcoHelper.MODID)
public class CommonProxy {

    public CommonProxy() {}

    public void start() {
        EcoHelper.LOGGER.info("CommonProxy start");
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EcoHelperConfig.init();
        registerListeners(bus);
        ModRecipes.register(bus);
        ModNetwork.register();
    }

    public void registerListeners(IEventBus bus) {}

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        new EcoCommand(event.getDispatcher());
    }

    @SubscribeEvent
    public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, TradableItemRecipe.Type.ID, TradableItemRecipe.Type.INSTANCE);
    }

    @SubscribeEvent
    public static void onPlayerJoinedServer(PlayerEvent.PlayerLoggedInEvent event) {
        String item = EcoHelperConfig.CONFIG.default_balance_unit.get();
        if (!Objects.equals(item, "minecraft:air")) {
            ResourceLocation itemRL = new ResourceLocation(item);
            ItemStack itemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(itemRL));
            Level level = event.getPlayer().getLevel();
            if (TradableItemRecipe.getRecipeFromItem(level, itemStack) == null) {
                EcoHelper.LOGGER.warn("*** Default balance unit " + item + " not found in recipe, using default unit instead ***");
                EcoHelper.LOGGER.warn("*** Please check your config or create a datapack to add this recipe ***");
                EcoHelper.LOGGER.warn("*** See https://github.com/BotW-Minecraft-Server/EcoHelper for details ***");
                EcoHelperConfig.CONFIG.default_balance_unit.set("minecraft:emerald");
                ResourceLocation defaultItemRL = new ResourceLocation("minecraft:emerald");
                ItemStack defaultItemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(defaultItemRL));
                EcoHelperConfig.CONFIG.default_balance_unit_worth.set(TradableItemRecipe.getRecipeFromItem(level, defaultItemStack).getWorth());
            }
            EcoHelper.ITEM_EXCHANGE_SWITCH = true;
        } else {
            EcoHelper.LOGGER.info("Default balance unit set to air, did not enable item worth system");
            EcoHelper.ITEM_EXCHANGE_SWITCH = false;
        }
        if (PlayerUtil.isJoiningWorldForTheFirstTime(event.getPlayer())) {
            if (EcoHelperConfig.CONFIG.default_balance.get() == 0) {
                return;
            } else {
                EcoUtils.EcoActionResult result = BalanceUtil.setBalance(event.getPlayer(), EcoHelperConfig.CONFIG.default_balance.get());
                if (result != EcoUtils.EcoActionResult.SUCCESS) {
                    EcoHelper.LOGGER.warn("Failed to add first join default balance to player " + event.getPlayer().getName().getString() + ", reason: " + result);
                } else {
                    EcoHelper.LOGGER.info("Added first join default balance to player " + event.getPlayer().getName().getString());
                }
            }
        }
    }
}
