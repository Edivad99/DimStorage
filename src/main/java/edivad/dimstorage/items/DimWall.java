package edivad.dimstorage.items;

import edivad.dimstorage.setup.ModSetup;
import net.minecraft.item.Item;

public class DimWall extends Item {

	public DimWall()
	{
		super(new Properties().group(ModSetup.dimStorageTab).maxStackSize(64));
	}
}
