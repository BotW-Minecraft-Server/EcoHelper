package link.botwmcs.samchai.ecohelper.network;

import link.botwmcs.samchai.ecohelper.EcoHelper;
import link.botwmcs.samchai.ecohelper.network.packet.ItemWorthAfterTaxC2SPacket;
import link.botwmcs.samchai.ecohelper.network.packet.ItemWorthAfterTaxS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }
    public static void register() {
         SimpleChannel net = NetworkRegistry.ChannelBuilder
                 .named(new ResourceLocation(EcoHelper.MODID, "main_network"))
                 .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

         INSTANCE = net;

         // Get item worth after tax:
         net.messageBuilder(ItemWorthAfterTaxC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                    .decoder(ItemWorthAfterTaxC2SPacket::new)
                    .encoder(ItemWorthAfterTaxC2SPacket::toBytes)
                    .consumer(ItemWorthAfterTaxC2SPacket::handle)
                    .add();
         net.messageBuilder(ItemWorthAfterTaxS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                    .decoder(ItemWorthAfterTaxS2CPacket::new)
                    .encoder(ItemWorthAfterTaxS2CPacket::toBytes)
                    .consumer(ItemWorthAfterTaxS2CPacket::handle)
                    .add();

         // Get player balance:

    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer serverPlayer) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), message);
    }

}
