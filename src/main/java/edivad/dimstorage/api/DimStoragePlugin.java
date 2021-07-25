package edivad.dimstorage.api;

import java.util.List;

import edivad.dimstorage.manager.DimStorageManager;
import net.minecraft.world.entity.player.Player;

public interface DimStoragePlugin {

    public AbstractDimStorage createDimStorage(DimStorageManager manager, Frequency freq);

    public String identifier();

    void sendClientInfo(Player player, List<AbstractDimStorage> list);

}
