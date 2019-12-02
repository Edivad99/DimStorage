package edivad.dimstorage.tabs;

import edivad.dimstorage.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class DimStorageTab extends CreativeTabs {

	public DimStorageTab(String label)
	{
		super(label);
	}

	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(ModBlocks.dimChest);
	}

}
