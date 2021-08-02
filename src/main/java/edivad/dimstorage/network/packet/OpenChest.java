package edivad.dimstorage.network.packet;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.storage.DimChestStorage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenChest {

    private Frequency freq;
    private boolean open;

    public OpenChest(FriendlyByteBuf buf) {
        freq = Frequency.readFromPacket(buf);
        open = buf.readBoolean();
    }

    public OpenChest(Frequency freq, boolean open) {
        this.freq = freq;
        this.open = open;
    }

    public void toBytes(FriendlyByteBuf buf) {
        freq.writeToPacket(buf);
        buf.writeBoolean(open);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ((DimChestStorage) DimStorageManager.instance(true).getStorage(freq, "item")).setClientOpen(open ? 1 : 0);
        });
        ctx.get().setPacketHandled(true);
    }
}
