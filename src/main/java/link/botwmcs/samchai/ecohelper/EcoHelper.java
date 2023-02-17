package link.botwmcs.samchai.ecohelper;

import com.mojang.logging.LogUtils;
import committee.nova.lighteco.util.EcoUtils;
import link.botwmcs.samchai.ecohelper.command.EcoCommand;
import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import link.botwmcs.samchai.ecohelper.gui.TradableItemsTooltip;
import link.botwmcs.samchai.ecohelper.item.TradableItems;
import link.botwmcs.samchai.ecohelper.item.TradableItemsManager;
import link.botwmcs.samchai.ecohelper.proxy.ClientProxy;
import link.botwmcs.samchai.ecohelper.proxy.CommonProxy;
import link.botwmcs.samchai.ecohelper.util.BalanceUtil;
import link.botwmcs.samchai.ecohelper.util.PlayerUtil;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.function.Function;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("ecohelper")
@Mod.EventBusSubscriber
public class EcoHelper {

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "ecohelper";
    public static boolean ITEM_EXCHANGE_SWITCH = true;
    public static CommonProxy PROXY;

    public EcoHelper() {
        PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        PROXY.start();
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
