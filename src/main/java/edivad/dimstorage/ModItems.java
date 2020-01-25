package edivad.dimstorage;

import edivad.dimstorage.items.DimCore;
import edivad.dimstorage.items.DimWall;
import edivad.dimstorage.items.ItemDimChest;
import edivad.dimstorage.items.SolidDimCore;
import edivad.dimstorage.items.dimpad.DimPad;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class ModItems {

	@ObjectHolder(Main.MODID + ":dim_core")
	public static DimCore dimCore;

	@ObjectHolder(Main.MODID + ":dim_wall")
	public static DimWall dimWall;

	@ObjectHolder(Main.MODID + ":solid_dim_core")
	public static SolidDimCore solidDimCore;
	
	@ObjectHolder(Main.MODID + ":dim_pad")
	public static DimPad dimPad;

	public static void register(IForgeRegistry<Item> registry)
	{
		registry.register(new DimCore());
		registry.register(new DimWall());
		registry.register(new SolidDimCore());
		registry.register(new ItemDimChest(ModBlocks.dimChest));
		registry.register(new DimPad());
	}
}
