package link.botwmcs.samchai.ecohelper;

import com.mojang.logging.LogUtils;
import link.botwmcs.samchai.ecohelper.command.EcoCommand;
import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("ecohelper")
@Mod.EventBusSubscriber
public class EcoHelper {

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public EcoHelper() {
        // Register config
        EcoHelperConfig.init();
    }

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        new EcoCommand(event.getDispatcher());
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
