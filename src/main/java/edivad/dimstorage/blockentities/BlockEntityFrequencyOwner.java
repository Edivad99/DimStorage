package edivad.dimstorage.blockentities;

import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.Frequency;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEntityFrequencyOwner extends BlockEntity implements MenuProvider {

  private final Frequency frequency = new Frequency();
  public boolean locked;
  private int changeCount;

  public BlockEntityFrequencyOwner(BlockEntityType<? extends BlockEntityFrequencyOwner> type,
      BlockPos pos, BlockState state) {
    super(type, pos, state);
    locked = false;
  }

  public static void serverTick(Level level, BlockPos pos, BlockState state,
      BlockEntityFrequencyOwner blockentity) {
    if (blockentity.getStorage().getChangeCount() > blockentity.changeCount) {
      level.updateNeighbourForOutputSignal(blockentity.worldPosition,
          blockentity.getBlockState().getBlock());
      blockentity.changeCount = blockentity.getStorage().getChangeCount();
    }
    blockentity.onServerTick(level, pos, state);
  }

  public static void clientTick(Level level, BlockPos pos, BlockState state,
      BlockEntityFrequencyOwner blockentity) {
    blockentity.onClientTick(level, pos, state);
  }

  public Frequency getFrequency() {
    return frequency.copy();
  }

  public void setFrequency(Frequency frequency) {
    this.frequency.set(frequency);
    this.setChanged();
    var state = level.getBlockState(worldPosition);
    level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
  }

  public void swapOwner(Player player) {
    if (frequency.hasOwner()) {
      setFrequency(getFrequency().setPublic());
    } else {
      setFrequency(getFrequency().setOwner(player));
    }
  }

  public void swapLocked() {
    locked = !locked;
    this.setChanged();
  }

  public boolean canAccess(Player player) {
    return frequency.canAccess(player);
  }

  public abstract AbstractDimStorage getStorage();

  public abstract void onServerTick(Level level, BlockPos pos, BlockState state);

  public abstract void onClientTick(Level level, BlockPos pos, BlockState state);

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    frequency.set(new Frequency(tag.getCompound("Frequency")));
    locked = tag.getBoolean("locked");
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("Frequency", frequency.serializeNBT());
    tag.putBoolean("locked", locked);
  }

  public InteractionResult activate(ServerPlayer player, Level level, BlockPos pos,
      InteractionHand hand) {
    if (canAccess(player)) {
      player.openMenu(this, buf -> buf.writeBlockPos(getBlockPos()).writeBoolean(false));
    } else {
      player.displayClientMessage(
          Component.literal("Access Denied!")
              .withStyle(ChatFormatting.RED), false);
    }
    return InteractionResult.SUCCESS;
  }

  //Synchronizing on chunk load
  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    tag.put("Frequency", frequency.serializeNBT());
    tag.putBoolean("locked", locked);
    return tag;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    setFrequency(new Frequency(tag.getCompound("Frequency")));
    locked = tag.getBoolean("locked");
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
  }
}
