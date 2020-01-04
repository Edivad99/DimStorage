package edivad.dimstorage;

import edivad.dimstorage.blocks.DimChest;
import edivad.dimstorage.container.ContainerDimChest;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

	//Block
	@ObjectHolder(Main.MODID + ":dimensional_chest")
	public static DimChest dimChest;
	
	
	//Container
	@ObjectHolder(Main.MODID + ":dimensional_chest")
	public static ContainerType<ContainerDimChest> containerDimChest;
	
	//TileEntity
	@ObjectHolder(Main.MODID + ":dimensional_chest")
	public static TileEntityType<?> tileEntityDimChest;
	
	public static void registerTiles(IForgeRegistry<TileEntityType<?>> registry)
	{
		registry.register(TileEntityType.Builder.create(TileEntityDimChest::new, ModBlocks.dimChest).build(null).setRegistryName(new ResourceLocation(Main.MODID, "dimensional_chest")));
	}
	
	public static void registerContainers(IForgeRegistry<ContainerType<?>> registry)
	{
		registry.register(IForgeContainerType.create((windowId, inv, data) ->
		{
			BlockPos pos = data.readBlockPos();
			TileEntity te = Main.proxy.getClientWorld().getTileEntity(pos);
			boolean isOpen = data.readBoolean();
			if(!(te instanceof TileEntityDimChest))
			{
				Main.logger.error("Wrong type of tile entity (expected TileEntityDimChest)!");
				return null;
			}
			
			TileEntityDimChest tile = (TileEntityDimChest) te;
			return new ContainerDimChest(windowId, Main.proxy.getClientPlayer().inventory, tile, isOpen);
		}).setRegistryName(new ResourceLocation(Main.MODID, "dimensional_chest")));
	}

	public static void register(IForgeRegistry<Block> registry)
	{
		registry.register(new DimChest());
	}
}
