package link.botwmcs.samchai.ecohelper.proxy;

import link.botwmcs.samchai.ecohelper.gui.TradableItemsTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.MinecraftForgeClient;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    public ClientProxy() {}

    @Override
    public void start() {
        super.start();
        registerTooltipComponents();
    }

    public static void registerTooltipComponents() {
        register();
    }
    private static <T extends ClientTooltipComponent & TooltipComponent> void register() {
        MinecraftForgeClient.registerTooltipComponentFactory((Class<T>) TradableItemsTooltip.WorthComponent.class, Function.identity());
    }
}
