package link.botwmcs.samchai.ecohelper.network.packet;

import link.botwmcs.samchai.ecohelper.EcoHelper;
import link.botwmcs.samchai.ecohelper.network.ModNetwork;
import link.botwmcs.samchai.ecohelper.util.BalanceUtil;
import link.botwmcs.samchai.ecohelper.util.DynamicUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ItemWorthAfterTaxC2SPacket {

    private double itemWorth;

    public ItemWorthAfterTaxC2SPacket(double itemWorth) {
        this.itemWorth = itemWorth;
    }

    public ItemWorthAfterTaxC2SPacket(FriendlyByteBuf buf) {
        this.itemWorth = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(itemWorth);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // SERVER SIDE
            ServerPlayer player = context.getSender();
            double balance = BalanceUtil.getBalance(player);
            double afterTaxItemWorth = DynamicUtil.getDynamicSellingPrice(itemWorth, balance);
            ModNetwork.sendToPlayer(new ItemWorthAfterTaxS2CPacket(afterTaxItemWorth), player);

        });
        return true;
    }
}
