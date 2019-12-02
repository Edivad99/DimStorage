package edivad.dimstorage.plugin;

import java.util.List;

import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.DimStoragePlugin;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.DimStorageSPH;
import edivad.dimstorage.storage.DimChestStorage;
import net.minecraft.entity.player.EntityPlayer;

public class DimChestPlugin implements DimStoragePlugin {

	public DimChestPlugin()
	{
	}

	@Override
	public AbstractDimStorage createDimStorage(DimStorageManager manager, Frequency freq)
	{
		return new DimChestStorage(manager, freq);
	}

	@Override
	public String identifer()
	{
		return "item";
	}

	@Override
	public void sendClientInfo(EntityPlayer player, List<AbstractDimStorage> list)
	{
		for(AbstractDimStorage inv : list)
		{
			if(((DimChestStorage) inv).getNumOpen() > 0)
			{
				DimStorageSPH.sendOpenUpdateTo(player, inv.freq, true);
			}
		}
	}
}
