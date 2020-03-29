package edivad.dimstorage.setup;

import edivad.dimstorage.Main;
import edivad.dimstorage.blocks.DimChest;
import edivad.dimstorage.blocks.DimTank;
import edivad.dimstorage.container.ContainerDimChest;
import edivad.dimstorage.container.ContainerDimTablet;
import edivad.dimstorage.container.ContainerDimTank;
import edivad.dimstorage.items.DimCore;
import edivad.dimstorage.items.DimTablet;
import edivad.dimstorage.items.DimWall;
import edivad.dimstorage.items.ItemDimChest;
import edivad.dimstorage.items.ItemDimTank;
import edivad.dimstorage.items.SolidDimCore;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tile.TileEntityDimTank;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration {

	private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Main.MODID);
	private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Main.MODID);
	private static final DeferredRegister<TileEntityType<?>> TILES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Main.MODID);
	private static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Main.MODID);

	public static void init()
	{
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
		CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static final RegistryObject<DimChest> DIMCHEST = BLOCKS.register("dimensional_chest", DimChest::new);
	public static final RegistryObject<Item> DIMCHEST_ITEM = ITEMS.register("dimensional_chest", ItemDimChest::new);
	public static final RegistryObject<TileEntityType<TileEntityDimChest>> DIMCHEST_TILE = TILES.register("dimensional_chest", () -> TileEntityType.Builder.create(TileEntityDimChest::new, DIMCHEST.get()).build(null));

	public static final RegistryObject<ContainerType<ContainerDimChest>> DIMCHEST_CONTAINER = CONTAINERS.register("dimensional_chest", () -> IForgeContainerType.create((windowId, inv, data) -> {
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
	}));
	
	
	public static final RegistryObject<DimTank> DIMTANK = BLOCKS.register("dimensional_tank", DimTank::new);
	public static final RegistryObject<Item> DIMTANK_ITEM = ITEMS.register("dimensional_tank", ItemDimTank::new);
	public static final RegistryObject<TileEntityType<TileEntityDimTank>> DIMTANK_TILE = TILES.register("dimensional_tank", () -> TileEntityType.Builder.create(TileEntityDimTank::new, DIMTANK.get()).build(null));
	public static final RegistryObject<ContainerType<ContainerDimTank>> DIMTANK_CONTAINER = CONTAINERS.register("dimensional_tank", () -> IForgeContainerType.create((windowId, inv, data) ->
	{
		BlockPos pos = data.readBlockPos();
		TileEntity te = Main.proxy.getClientWorld().getTileEntity(pos);
		boolean isOpen = data.readBoolean();
		if(!(te instanceof TileEntityDimTank))
		{
			Main.logger.error("Wrong type of tile entity (expected TileEntityDimTank)!");
			return null;
		}

		TileEntityDimTank tile = (TileEntityDimTank) te;
		return new ContainerDimTank(windowId, Main.proxy.getClientPlayer().inventory, tile, isOpen);
	}));

	public static final RegistryObject<DimCore> DIMCORE = ITEMS.register("dim_core", DimCore::new);
	public static final RegistryObject<DimWall> DIMWALL = ITEMS.register("dim_wall", DimWall::new);
	public static final RegistryObject<SolidDimCore> SOLIDDIMCORE = ITEMS.register("solid_dim_core", SolidDimCore::new);

	public static final RegistryObject<DimTablet> DIMPAD = ITEMS.register("dimensional_tablet", DimTablet::new);
	public static final RegistryObject<ContainerType<ContainerDimTablet>> DIMPAD_CONTAINER = CONTAINERS.register("dimensional_tablet", () -> IForgeContainerType.create((windowId, inv, data) -> {
		return new ContainerDimTablet(windowId, Main.proxy.getClientPlayer().inventory, Main.proxy.getClientWorld());
	}));
}