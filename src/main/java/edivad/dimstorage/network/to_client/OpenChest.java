package edivad.dimstorage.network.to_client;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.storage.DimChestStorage;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenChest(Frequency freq, boolean open) implements CustomPacketPayload {

  public static final Type<OpenChest> TYPE =
      new Type<>(DimStorage.id("open_chest"));

  public static final StreamCodec<RegistryFriendlyByteBuf, OpenChest> STREAM_CODEC =
      StreamCodec.composite(
          Frequency.STREAM_CODEC, OpenChest::freq,
          ByteBufCodecs.BOOL, OpenChest::open,
          OpenChest::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(OpenChest message, IPayloadContext ctx) {
    var level = ctx.player().level();
    ((DimChestStorage) DimStorageManager.instance(level)
        .getStorage(message.freq, "item"))
        .setClientOpen(message.open ? 1 : 0);
  }
}
