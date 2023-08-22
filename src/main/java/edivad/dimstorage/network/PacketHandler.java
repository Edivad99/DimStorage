package edivad.dimstorage.network;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.network.packet.OpenChest;
import edivad.dimstorage.network.packet.SyncLiquidTank;
import edivad.dimstorage.network.packet.UpdateDimChest;
import edivad.dimstorage.network.packet.UpdateDimTank;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

  private static final String PROTOCOL_VERSION = "1";
  public static SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
      .named(new ResourceLocation(DimStorage.ID, "network"))
      .clientAcceptedVersions(PROTOCOL_VERSION::equals)
      .serverAcceptedVersions(PROTOCOL_VERSION::equals)
      .networkProtocolVersion(() -> PROTOCOL_VERSION)
      .simpleChannel();

  public static void init() {
    int id = 0;
    INSTANCE
        .messageBuilder(UpdateDimChest.class, 0, NetworkDirection.PLAY_TO_SERVER)
        .encoder(UpdateDimChest::encode)
        .decoder(UpdateDimChest::new)
        .consumerMainThread(UpdateDimChest::handle)
        .add();
    INSTANCE
        .messageBuilder(OpenChest.class, 1, NetworkDirection.PLAY_TO_CLIENT)
        .encoder(OpenChest::encode)
        .decoder(OpenChest::decode)
        .consumerMainThread(OpenChest::handle)
        .add();
    INSTANCE
        .messageBuilder(UpdateDimTank.class, 2, NetworkDirection.PLAY_TO_SERVER)
        .encoder(UpdateDimTank::encode)
        .decoder(UpdateDimTank::new)
        .consumerMainThread(UpdateDimTank::handle)
        .add();
    INSTANCE
        .messageBuilder(SyncLiquidTank.class, 3, NetworkDirection.PLAY_TO_CLIENT)
        .encoder(SyncLiquidTank::encode)
        .decoder(SyncLiquidTank::decode)
        .consumerMainThread(SyncLiquidTank::handle)
        .add();
  }
}
