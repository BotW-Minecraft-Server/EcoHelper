package link.botwmcs.samchai.ecohelper.network.packet;

import link.botwmcs.samchai.ecohelper.EcoHelper;
import link.botwmcs.samchai.ecohelper.gui.TradableItemsTooltip;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ItemWorthAfterTaxS2CPacket {

    private double balance;

    public ItemWorthAfterTaxS2CPacket(double balance) {
        this.balance = balance;
    }

    public ItemWorthAfterTaxS2CPacket(FriendlyByteBuf buf) {
        balance = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(balance);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // Motherfucker this bullshit:
            // Old: TradableItemsTooltip.itemWorthAfterTaxFromServer = balance;
            // It didn't work. Now let's try this:
            TradableItemsTooltip.itemWorthAfterTaxFromServer = balance;
        });
        return true;
    }
}
