package link.botwmcs.samchai.ecohelper;

import com.mojang.logging.LogUtils;
import committee.nova.lighteco.util.EcoUtils;
import link.botwmcs.samchai.ecohelper.command.EcoCommand;
import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import link.botwmcs.samchai.ecohelper.item.TradableItems;
import link.botwmcs.samchai.ecohelper.item.TradableItemsManager;
import link.botwmcs.samchai.ecohelper.util.BalanceUtil;
import link.botwmcs.samchai.ecohelper.util.PlayerUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.Objects;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("ecohelper")
@Mod.EventBusSubscriber
public class EcoHelper {

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "ecohelper";
    public static boolean ITEM_EXCHANGE_SWITCH = true;

    public EcoHelper() {
        // Register config
        EcoHelperConfig.init();
    }

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        new EcoCommand(event.getDispatcher());
    }

    // Player join server give money
    @SubscribeEvent
    public static void onPlayerJoinedServer(PlayerEvent.PlayerLoggedInEvent event) {

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

    // Register tradable items from config
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        String item = EcoHelperConfig.CONFIG.default_balance_unit.get();
        if (!Objects.equals(item, "minecraft:air")) {
            ResourceLocation itemRL = new ResourceLocation(item);
            ItemStack itemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(itemRL));
            if (TradableItemsManager.get(itemStack) == null) {
                EcoHelper.LOGGER.warn("*** Default balance unit " + item + " not found in data, using default unit instead ***");
                EcoHelper.LOGGER.warn("*** Please check your config or create a datapack to add this item ***");
                EcoHelper.LOGGER.warn("*** See https://github.com/BotW-Minecraft-Server/EcoHelper for details ***");
                EcoHelperConfig.CONFIG.default_balance_unit.set("minecraft:emerald");
                ResourceLocation defaultItemRL = new ResourceLocation("minecraft:emerald");
                ItemStack defaultItemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(defaultItemRL));
                TradableItems defaultItem = TradableItemsManager.get(defaultItemStack);
                assert defaultItem != null;
                double defaultItemWorth = defaultItem.worth();
                EcoHelperConfig.CONFIG.default_balance_unit_worth.set(defaultItemWorth);
            }
            ITEM_EXCHANGE_SWITCH = true;
        } else {
            EcoHelper.LOGGER.info("Default balance unit set to air, did not enable item worth system");
            ITEM_EXCHANGE_SWITCH = false;
        }
    }

    public static ResourceLocation resourceLocation(String path) {
        return new ResourceLocation(MODID, path);
    }

//    private void setup(final FMLCommonSetupEvent event) {
//        // Some preinit code
//        LOGGER.info("HELLO FROM PREINIT");
//        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
//    }
//
//    private void enqueueIMC(final InterModEnqueueEvent event) {
//        // Some example code to dispatch IMC to another mod
//        InterModComms.sendTo("EcoHelper", "helloworld", () -> {
//            LOGGER.info("Hello world from the MDK");
//            return "Hello world";
//        });
//    }
//
//    private void processIMC(final InterModProcessEvent event) {
//        // Some example code to receive and process InterModComms from other mods
//        LOGGER.info("Got IMC {}", event.getIMCStream().
//                map(m -> m.messageSupplier().get()).
//                collect(Collectors.toList()));
//    }
//
//    // You can use SubscribeEvent and let the Event Bus discover methods to call
//    @SubscribeEvent
//    public void onServerStarting(ServerStartingEvent event) {
//        // Do something when the server starts
//        LOGGER.info("HELLO from server starting");
//    }
//
//    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
//    // Event bus for receiving Registry Events)
//    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
//    public static class RegistryEvents {
//        @SubscribeEvent
//        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
//            // Register a new block here
//            LOGGER.info("HELLO from Register Block");
//        }
//    }
}
