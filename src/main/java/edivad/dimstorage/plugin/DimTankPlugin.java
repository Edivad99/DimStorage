package edivad.dimstorage.plugin;

import java.util.List;
import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.DimStoragePlugin;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.storage.DimTankStorage;
import net.minecraft.world.entity.player.Player;

public class DimTankPlugin implements DimStoragePlugin {

  @Override
  public AbstractDimStorage createDimStorage(DimStorageManager manager, Frequency freq) {
    return new DimTankStorage(manager, freq);
  }

  @Override
  public String identifier() {
    return "fluid";
  }

  @Override
  public void sendClientInfo(Player player, List<AbstractDimStorage> list) {
  }
}
