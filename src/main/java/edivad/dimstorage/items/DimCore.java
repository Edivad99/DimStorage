package edivad.dimstorage.items;

import edivad.dimstorage.setup.ModSetup;
import net.minecraft.item.Item;

public class DimCore extends Item {

	public DimCore()
	{
		super(new Properties().group(ModSetup.dimStorageTab).maxStackSize(64));
	}
}
