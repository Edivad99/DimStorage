package edivad.dimstorage.network.packet;

import java.util.function.Supplier;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.storage.DimChestStorage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record OpenChest (Frequency freq, boolean open) {

  public static OpenChest decode(FriendlyByteBuf buf) {
    return new OpenChest(Frequency.readFromPacket(buf), buf.readBoolean());
  }

  public void encode(FriendlyByteBuf buf) {
    freq.writeToPacket(buf);
    buf.writeBoolean(open);
  }

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ((DimChestStorage) DimStorageManager.instance(true)
          .getStorage(freq, "item"))
          .setClientOpen(open ? 1 : 0);
    });
    ctx.get().setPacketHandled(true);
  }
}
