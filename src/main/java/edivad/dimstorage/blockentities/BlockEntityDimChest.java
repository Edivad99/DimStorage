package edivad.dimstorage.blockentities;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.menu.DimChestMenu;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.storage.DimChestStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class BlockEntityDimChest extends BlockEntityFrequencyOwner {

  private static final float MIN_MOVABLE_POSITION = 0f;
  private static final float MAX_MOVABLE_POSITION = 0.5f;
  private static final float OPENING_SPEED = 0.05f;
  public float movablePartState;
  public int rotation;
  private int openCount;

  public BlockEntityDimChest(BlockPos pos, BlockState state) {
    super(Registration.DIMCHEST_TILE.get(), pos, state);
    movablePartState = MIN_MOVABLE_POSITION;
  }

  @Override
  public void onServerTick(Level level, BlockPos pos, BlockState state) {
    if (level.getGameTime() % 20 == 0 || openCount != getStorage().getNumOpen()) {
      openCount = getStorage().getNumOpen();
      level.blockEvent(pos, state.getBlock(), 1, openCount);
      level.updateNeighborsAt(worldPosition, state.getBlock());
    }

    commonTick();
  }

  @Override
  public void onClientTick(Level level, BlockPos pos, BlockState state) {
    commonTick();
  }

  private void commonTick() {
    if (openCount > 0) //Both server and client
    {
      if (movablePartState < MAX_MOVABLE_POSITION) {
        movablePartState += OPENING_SPEED;
      } else {
        movablePartState = MAX_MOVABLE_POSITION;
      }
    } else {
      if (movablePartState > MIN_MOVABLE_POSITION) {
        movablePartState -= OPENING_SPEED;
      } else {
        movablePartState = MIN_MOVABLE_POSITION;
      }
    }
  }

  public int getComparatorInput() {
    return Optional.ofNullable(getItemHandler(null))
        .map(ItemHandlerHelper::calcRedstoneFromInventory)
        .orElse(0);
  }

  @Nullable
  public IItemHandler getItemHandler(Direction direction) {
    return locked ? null : new InvWrapper(getStorage());
  }

  @Override
  public boolean triggerEvent(int id, int type) {
    if (id == 1) {
      openCount = type;
      return true;
    }
    return false;
  }

  @Override
  public DimChestStorage getStorage() {
    return (DimChestStorage) DimStorageManager.instance(level.isClientSide)
        .getStorage(getFrequency(), "item");
  }

  public void onPlaced(LivingEntity entity) {
    rotation = (int) Math.floor(entity.getYRot() * 4 / 360 + 2.5D) & 3;
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putByte("rot", (byte) rotation);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    rotation = tag.getByte("rot") & 3;
  }

  //Synchronizing on block update
  @Override
  public final ClientboundBlockEntityDataPacket getUpdatePacket() {
    CompoundTag root = new CompoundTag();
    root.put("Frequency", getFrequency().serializeNBT());
    root.putBoolean("locked", locked);
    root.putByte("rot", (byte) rotation);
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    CompoundTag tag = pkt.getTag();
    setFrequency(new Frequency(tag.getCompound("Frequency")));
    locked = tag.getBoolean("locked");
    rotation = tag.getByte("rot") & 3;
  }

  //Synchronizing on chunk load
  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    tag.putByte("rot", (byte) rotation);
    return tag;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    super.handleUpdateTag(tag);
    rotation = tag.getByte("rot") & 3;
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new DimChestMenu(id, inventory, this, false);
  }
}
