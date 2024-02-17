package edivad.dimstorage.network;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.network.to_client.OpenChest;
import edivad.dimstorage.network.to_client.SyncLiquidTank;
import edivad.dimstorage.network.to_server.UpdateDimChest;
import edivad.dimstorage.network.to_server.UpdateDimTank;
import edivad.edivadlib.network.BasePacketHandler;
import edivad.edivadlib.network.EdivadLibPacket;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;

public class PacketHandler extends BasePacketHandler {

  public PacketHandler(IEventBus modEventBus) {
    super(modEventBus, DimStorage.ID, "1");
  }

  @Override
  protected void registerClientToServer(PacketRegistrar registrar) {
    registrar.play(UpdateDimChest.ID, UpdateDimChest::read);
    registrar.play(UpdateDimTank.ID, UpdateDimTank::read);
  }

  @Override
  protected void registerServerToClient(PacketRegistrar registrar) {
    registrar.play(OpenChest.ID, OpenChest::read);
    registrar.play(SyncLiquidTank.ID, SyncLiquidTank::read);
  }

  public static void sendToServer(EdivadLibPacket packet) {
    PacketDistributor.SERVER.noArg().send(packet);
  }

  public static void sendToAll(EdivadLibPacket packet) {
    PacketDistributor.ALL.noArg().send(packet);
  }
}
