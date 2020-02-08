package edivad.dimstorage.items;

import edivad.dimstorage.setup.ModSetup;
import net.minecraft.item.Item;

public class SolidDimCore extends Item {

	public SolidDimCore()
	{
		super(new Properties().group(ModSetup.dimStorageTab).maxStackSize(64));
	}
}
