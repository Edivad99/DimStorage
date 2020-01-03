package edivad.dimstorage.tabs;

import edivad.dimstorage.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class DimStorageTab extends ItemGroup {

	public DimStorageTab(String label)
	{
		super(label);
	}

	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(ModBlocks.dimChest);
	}

}
