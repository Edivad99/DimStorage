package edivad.dimstorage;

import edivad.dimstorage.blocks.DimChest;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

	@GameRegistry.ObjectHolder(Main.MODID + ":dimensional_chest")
	public static DimChest dimChest;

	@SideOnly(Side.CLIENT)
	public static void initModels()
	{
		dimChest.initModel();
	}

	public static void register(IForgeRegistry<Block> registry)
	{
		registry.register(new DimChest());

		GameRegistry.registerTileEntity(TileEntityDimChest.class, Main.MODID + ":dimensional_chest");
	}
}
