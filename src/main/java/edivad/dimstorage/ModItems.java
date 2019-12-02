package edivad.dimstorage;

import edivad.dimstorage.items.DimCore;
import edivad.dimstorage.items.DimWall;
import edivad.dimstorage.items.ItemDimChest;
import edivad.dimstorage.items.SolidDimCore;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

	@GameRegistry.ObjectHolder(Main.MODID + ":dim_core")
	public static DimCore dimCore;

	@GameRegistry.ObjectHolder(Main.MODID + ":dim_wall")
	public static DimWall dimWall;

	@GameRegistry.ObjectHolder(Main.MODID + ":solid_dim_core")
	public static SolidDimCore solidDimCore;

	@SideOnly(Side.CLIENT)
	public static void initModels()
	{
		dimCore.initModel();
		dimWall.initModel();
		solidDimCore.initModel();
	}

	public static void register(IForgeRegistry<Item> registry)
	{
		registry.register(new DimCore());
		registry.register(new DimWall());
		registry.register(new SolidDimCore());
		registry.register(new ItemDimChest(ModBlocks.dimChest));
	}
}
