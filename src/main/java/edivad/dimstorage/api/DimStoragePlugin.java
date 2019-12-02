package edivad.dimstorage.api;

import java.util.List;

import edivad.dimstorage.manager.DimStorageManager;
import net.minecraft.entity.player.EntityPlayer;

public interface DimStoragePlugin {

	public AbstractDimStorage createDimStorage(DimStorageManager manager, Frequency freq);

	public String identifer();

	void sendClientInfo(EntityPlayer player, List<AbstractDimStorage> list);

}
