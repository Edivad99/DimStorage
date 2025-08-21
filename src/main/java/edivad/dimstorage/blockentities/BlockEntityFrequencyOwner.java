package edivad.dimstorage.blockentities;

import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.items.components.DimStorageComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
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

  private Frequency frequency = new Frequency();
  public boolean locked;
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

  public void swapLocked() {
    this.locked = !this.locked;
    this.setChanged();
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
    this.frequency = tag.read("frequency", Frequency.CODEC).orElseThrow();
    this.locked = tag.getBoolean("locked").orElseThrow();
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    tag.store("frequency", Frequency.CODEC, this.frequency);
    tag.putBoolean("locked", this.locked);
  }

  @Override
  protected void applyImplicitComponents(DataComponentGetter componentGetter) {
    var frequency = componentGetter.get(DimStorageComponents.FREQUENCY);
    if (frequency != null) {
      this.setFrequency(frequency);
    }
  }

  @Override
  protected void collectImplicitComponents(DataComponentMap.Builder components) {
    components.set(DimStorageComponents.FREQUENCY, this.frequency);
  }

  public InteractionResult useItemOn(ServerPlayer player, Level level, BlockPos pos,
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
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    var tag = super.getUpdateTag(registries);
    tag.store("frequency", Frequency.CODEC, this.frequency);
    tag.putBoolean("locked", this.locked);
    return tag;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
    super.handleUpdateTag(tag, lookupProvider);
    this.setFrequency(tag.read("frequency", Frequency.CODEC).orElseThrow());
    this.locked = tag.getBoolean("locked").orElse(false);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
  }
}
