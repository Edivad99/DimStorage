package edivad.dimstorage.blockentities;

import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.items.components.DimStorageComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEntityFrequencyOwner extends BlockEntity implements MenuProvider {

  private Frequency frequency = new Frequency();
  private boolean locked;
  private int changeCount;

  public BlockEntityFrequencyOwner(BlockEntityType<? extends BlockEntityFrequencyOwner> type,
      BlockPos pos, BlockState state) {
    super(type, pos, state);
    this.locked = false;
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
    return this.frequency;
  }

  public void setFrequency(Frequency frequency) {
    this.frequency = frequency;
    this.invalidateCapabilities();
    this.setChanged();
    var state = this.level.getBlockState(this.worldPosition);
    this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_ALL);
  }

  public void swapOwner(Player player) {
    if (this.frequency.hasOwner()) {
      setFrequency(this.frequency.setPublic());
    } else {
      setFrequency(this.frequency.setOwner(player));
    }
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
    this.invalidateCapabilities();
    this.setChanged();
  }

  public boolean isLocked() {
    return this.locked;
  }

  public void swapLocked() {
    this.setLocked(!this.locked);
  }

  public boolean canAccess(Player player) {
    return this.frequency.canAccess(player);
  }

  public abstract AbstractDimStorage getStorage();

  public abstract void onServerTick(Level level, BlockPos pos, BlockState state);

  public abstract void onClientTick(Level level, BlockPos pos, BlockState state);

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    this.frequency = Frequency.deserializeNBT(tag.getCompound("frequency"));
    this.locked = tag.getBoolean("locked");
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    tag.put("frequency", this.frequency.serializeNBT());
    tag.putBoolean("locked", this.locked);
  }

  @Override
  protected void applyImplicitComponents(DataComponentInput componentInput) {
    var frequency = componentInput.get(DimStorageComponents.FREQUENCY);
    if (frequency != null) {
      this.setFrequency(frequency);
    }
  }

  @Override
  protected void collectImplicitComponents(DataComponentMap.Builder components) {
    components.set(DimStorageComponents.FREQUENCY, this.frequency);
  }

  public ItemInteractionResult useItemOn(ServerPlayer player, Level level, BlockPos pos,
      InteractionHand hand) {
    if (canAccess(player)) {
      player.openMenu(this, buf -> buf.writeBlockPos(getBlockPos()).writeBoolean(false));
    } else {
      player.displayClientMessage(
          Component.literal("Access Denied!")
              .withStyle(ChatFormatting.RED), false);
    }
    return ItemInteractionResult.SUCCESS;
  }

  //Synchronizing on chunk load
  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    var tag = super.getUpdateTag(registries);
    tag.put("frequency", this.frequency.serializeNBT());
    tag.putBoolean("locked", this.locked);
    return tag;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
    super.handleUpdateTag(tag, lookupProvider);
    this.setFrequency(Frequency.deserializeNBT(tag.getCompound("frequency")));
    this.locked = tag.getBoolean("locked");
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
  }
}
