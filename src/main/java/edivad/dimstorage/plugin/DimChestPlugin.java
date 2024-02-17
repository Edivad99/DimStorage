package edivad.dimstorage.plugin;

import java.util.List;
import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.DimStoragePlugin;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.to_client.OpenChest;
import edivad.dimstorage.storage.DimChestStorage;
import net.minecraft.world.entity.player.Player;

public class DimChestPlugin implements DimStoragePlugin {

  @Override
  public AbstractDimStorage createDimStorage(DimStorageManager manager, Frequency freq) {
    return new DimChestStorage(manager, freq);
  }

  @Override
  public String identifier() {
    return "item";
  }

  @Override
  public void sendClientInfo(Player player, List<AbstractDimStorage> list) {
    for (var inv : list) {
      if (((DimChestStorage) inv).getNumOpen() > 0) {
        PacketHandler.sendToAll(new OpenChest(inv.freq, true));
      }
    }
  }
}
