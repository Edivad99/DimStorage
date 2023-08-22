package edivad.dimstorage.setup;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentities.BlockEntityDimChest;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.blocks.DimChest;
import edivad.dimstorage.blocks.DimTank;
import edivad.dimstorage.container.ContainerDimChest;
import edivad.dimstorage.container.ContainerDimTablet;
import edivad.dimstorage.container.ContainerDimTank;
import edivad.dimstorage.items.DimTablet;
import edivad.dimstorage.items.ItemDimBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registration {

  private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
      ForgeRegistries.BLOCKS, DimStorage.ID);
  public static final RegistryObject<DimChest> DIMCHEST = BLOCKS.register("dimensional_chest",
      DimChest::new);
  public static final RegistryObject<DimTank> DIMTANK = BLOCKS.register("dimensional_tank",
      DimTank::new);
  private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
      DimStorage.ID);
  public static final RegistryObject<Item> DIMCHEST_ITEM = ITEMS.register("dimensional_chest",
      () -> new ItemDimBase(DIMCHEST.get()));
  public static final RegistryObject<Item> DIMTANK_ITEM = ITEMS.register("dimensional_tank",
      () -> new ItemDimBase(DIMTANK.get()));
  public static final RegistryObject<Item> DIMCORE = ITEMS.register("dim_core",
      () -> new Item(new Item.Properties()));
  public static final RegistryObject<Item> DIMWALL = ITEMS.register("dim_wall",
      () -> new Item(new Item.Properties()));
  public static final RegistryObject<Item> SOLIDDIMCORE = ITEMS.register("solid_dim_core",
      () -> new Item(new Item.Properties()));
  public static final RegistryObject<DimTablet> DIMTABLET = ITEMS.register("dimensional_tablet",
      DimTablet::new);
  private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(
      ForgeRegistries.BLOCK_ENTITY_TYPES, DimStorage.ID);  public static final RegistryObject<BlockEntityType<BlockEntityDimChest>> DIMCHEST_TILE = TILES.register(
      "dimensional_chest",
      () -> BlockEntityType.Builder.of(BlockEntityDimChest::new, DIMCHEST.get()).build(null));
  private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(
      ForgeRegistries.MENU_TYPES, DimStorage.ID);
  public static final RegistryObject<MenuType<ContainerDimChest>> DIMCHEST_CONTAINER = CONTAINERS.register(
      "dimensional_chest", () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        BlockEntity te = inv.player.getCommandSenderWorld().getBlockEntity(pos);
        boolean isOpen = data.readBoolean();
        if (!(te instanceof BlockEntityDimChest blockentity)) {
          DimStorage.LOGGER.error("Wrong type of blockentity (expected BlockEntityDimChest)!");
          return null;
        }

        return new ContainerDimChest(windowId, inv.player.getInventory(), blockentity, isOpen);
      }));
  public static final RegistryObject<MenuType<ContainerDimTank>> DIMTANK_CONTAINER = CONTAINERS.register(
      "dimensional_tank", () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        BlockEntity te = inv.player.getCommandSenderWorld().getBlockEntity(pos);
        boolean isOpen = data.readBoolean();
        if (!(te instanceof BlockEntityDimTank blockentity)) {
          DimStorage.LOGGER.error("Wrong type of blockentity (expected BlockEntityDimTank)!");
          return null;
        }

        return new ContainerDimTank(windowId, inv.player.getInventory(), blockentity, isOpen);
      }));
  public static final RegistryObject<MenuType<ContainerDimTablet>> DIMTABLET_CONTAINER = CONTAINERS.register(
      "dimensional_tablet", () -> IForgeMenuType.create(
          (windowId, inv, data) -> new ContainerDimTablet(windowId, inv.player.getInventory(),
              inv.player.getCommandSenderWorld())));

  public static void init(IEventBus eventBus) {
    BLOCKS.register(eventBus);
    ITEMS.register(eventBus);
    TILES.register(eventBus);
    CONTAINERS.register(eventBus);
  }



  public static final RegistryObject<BlockEntityType<BlockEntityDimTank>> DIMTANK_TILE = TILES.register(
      "dimensional_tank",
      () -> BlockEntityType.Builder.of(BlockEntityDimTank::new, DIMTANK.get()).build(null));


}