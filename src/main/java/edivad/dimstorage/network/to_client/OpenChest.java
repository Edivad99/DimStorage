package edivad.dimstorage.network.to_client;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.storage.DimChestStorage;
import edivad.edivadlib.network.EdivadLibPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record OpenChest (Frequency freq, boolean open) implements EdivadLibPacket {

  public static final ResourceLocation ID = DimStorage.rl("open_chest");

  public static OpenChest read(FriendlyByteBuf buf) {
    return new OpenChest(Frequency.readFromPacket(buf), buf.readBoolean());
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    freq.writeToPacket(buf);
    buf.writeBoolean(open);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext context) {
    context.level().ifPresent(level -> {
      ((DimChestStorage) DimStorageManager.instance(level)
          .getStorage(freq, "item"))
          .setClientOpen(open ? 1 : 0);
    });
  }
}
