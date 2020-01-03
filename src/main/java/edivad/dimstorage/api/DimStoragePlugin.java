package edivad.dimstorage.api;

import java.util.List;

import edivad.dimstorage.manager.DimStorageManager;
import net.minecraft.entity.player.PlayerEntity;

public interface DimStoragePlugin {

	public AbstractDimStorage createDimStorage(DimStorageManager manager, Frequency freq);

	public String identifer();

	void sendClientInfo(PlayerEntity player, List<AbstractDimStorage> list);

}
