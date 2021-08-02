package edivad.dimstorage.api;

import edivad.dimstorage.manager.DimStorageManager;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public interface DimStoragePlugin {

    AbstractDimStorage createDimStorage(DimStorageManager manager, Frequency freq);

    String identifier();

    void sendClientInfo(Player player, List<AbstractDimStorage> list);

}
