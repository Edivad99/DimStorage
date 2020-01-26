package edivad.dimstorage.plugin;

import java.util.List;

import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.DimStoragePlugin;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.storage.DimTankStorage;
import net.minecraft.entity.player.PlayerEntity;

public class DimTankPlugin implements DimStoragePlugin {

	public DimTankPlugin()
	{

	}

	@Override
	public AbstractDimStorage createDimStorage(DimStorageManager manager, Frequency freq)
	{
		return new DimTankStorage(manager, freq);
	}

	@Override
	public String identifer()
	{
		return "fluid";
	}

	@Override
	public void sendClientInfo(PlayerEntity player, List<AbstractDimStorage> list)
	{
	}
}
