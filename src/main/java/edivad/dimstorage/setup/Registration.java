package edivad.dimstorage.setup;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentities.BlockEntityDimChest;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.blocks.DimChestBlock;
import edivad.dimstorage.blocks.DimTankBlock;
import edivad.dimstorage.items.DimTablet;
import edivad.dimstorage.items.ItemDimBase;
import edivad.dimstorage.menu.DimChestMenu;
import edivad.dimstorage.menu.DimTabletMenu;
import edivad.dimstorage.menu.DimTankMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Registration {

  private static final DeferredRegister.Blocks BLOCKS =
      DeferredRegister.createBlocks(DimStorage.ID);
  private static final DeferredRegister.Items ITEMS =
      DeferredRegister.createItems(DimStorage.ID);
  private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY =
      DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, DimStorage.ID);
  private static final DeferredRegister<MenuType<?>> MENU =
      DeferredRegister.create(BuiltInRegistries.MENU, DimStorage.ID);


  public static final DeferredBlock<DimChestBlock> DIMCHEST =
      BLOCKS.registerBlock("dimensional_chest", DimChestBlock::new);
  public static final DeferredBlock<DimTankBlock> DIMTANK =
      BLOCKS.registerBlock("dimensional_tank", DimTankBlock::new);

  public static final DeferredItem<BlockItem> DIMCHEST_ITEM =
      ITEMS.registerItem("dimensional_chest", properties ->
          new ItemDimBase(DIMCHEST.get(), properties.useBlockDescriptionPrefix()));
  public static final DeferredItem<BlockItem> DIMTANK_ITEM =
      ITEMS.registerItem("dimensional_tank", properties ->
          new ItemDimBase(DIMTANK.get(), properties.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> DIMCORE =
      ITEMS.registerSimpleItem("dim_core");
  public static final DeferredItem<Item> DIMWALL =
      ITEMS.registerSimpleItem("dim_wall");
  public static final DeferredItem<Item> SOLIDDIMCORE =
      ITEMS.registerSimpleItem("solid_dim_core");
  public static final DeferredItem<DimTablet> DIMTABLET =
      ITEMS.registerItem("dimensional_tablet", properties ->
          new DimTablet(properties.stacksTo(1)));

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityDimChest>> DIMCHEST_TILE =
      BLOCK_ENTITY.register("dimensional_chest", () -> new BlockEntityType<>(BlockEntityDimChest::new, DIMCHEST.get()));
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityDimTank>> DIMTANK_TILE =
      BLOCK_ENTITY.register("dimensional_tank", () -> new BlockEntityType<>(BlockEntityDimTank::new, DIMTANK.get()));

  public static final DeferredHolder<MenuType<?>, MenuType<DimChestMenu>> DIMCHEST_MENU =
      MENU.register("dimensional_chest", () ->
          new MenuType<>((IContainerFactory<DimChestMenu>) (id, inventory, buf) -> {
            var pos = buf.readBlockPos();
            var blockEntity = inventory.player.level().getBlockEntity(pos);
            boolean isOpen = buf.readBoolean();
            if (!(blockEntity instanceof BlockEntityDimChest chest)) {
              DimStorage.LOGGER.error("Wrong type of block entity (expected BlockEntityDimChest)!");
              return null;
            }
            return new DimChestMenu(id, inventory.player.getInventory(), chest, isOpen);
          }, FeatureFlags.DEFAULT_FLAGS));
  public static final DeferredHolder<MenuType<?>, MenuType<DimTankMenu>> DIMTANK_MENU =
      MENU.register("dimensional_tank", () ->
          new MenuType<>((IContainerFactory<DimTankMenu>) (id, inventory, buf) -> {
            var pos = buf.readBlockPos();
            var blockEntity = inventory.player.level().getBlockEntity(pos);
            boolean isOpen = buf.readBoolean();
            if (!(blockEntity instanceof BlockEntityDimTank tank)) {
              DimStorage.LOGGER.error("Wrong type of block entity (expected BlockEntityDimTank)!");
              return null;
            }
            return new DimTankMenu(id, inventory.player.getInventory(), tank, isOpen);
          }, FeatureFlags.DEFAULT_FLAGS));

  public static final DeferredHolder<MenuType<?>, MenuType<DimTabletMenu>> DIMTABLET_MENU =
      MENU.register("dimensional_tablet", () ->
          new MenuType<>((IContainerFactory<DimTabletMenu>) (id, inventory, buf) ->
              new DimTabletMenu(id, inventory.player.getInventory(), inventory.player.level()),
              FeatureFlags.DEFAULT_FLAGS));

  public static void init(IEventBus eventBus) {
    BLOCKS.register(eventBus);
    ITEMS.register(eventBus);
    BLOCK_ENTITY.register(eventBus);
    MENU.register(eventBus);
  }
}
