package edivad.dimstorage.api;

import java.util.List;
import edivad.dimstorage.manager.DimStorageManager;
import net.minecraft.world.entity.player.Player;

public interface DimStoragePlugin {

  AbstractDimStorage createDimStorage(DimStorageManager manager, Frequency freq);

  String identifier();

  void sendClientInfo(Player player, List<AbstractDimStorage> list);

}
