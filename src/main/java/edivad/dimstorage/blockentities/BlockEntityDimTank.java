package edivad.dimstorage.blockentities;

import org.jetbrains.annotations.Nullable;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.menu.DimTankMenu;
import edivad.dimstorage.network.TankState;
import edivad.dimstorage.network.to_client.SyncLiquidTank;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.storage.DimTankStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.network.PacketDistributor;

public class BlockEntityDimTank extends BlockEntityFrequencyOwner {

  public DimTankState liquidState;
  public boolean autoEject = false;

  public BlockEntityDimTank(BlockPos pos, BlockState state) {
    super(Registration.DIMTANK_TILE.get(), pos, state);
    this.liquidState = new DimTankState(getFrequency());
  }

  @Override
  public void onServerTick(Level level, BlockPos pos, BlockState state) {
    if (this.autoEject) {
      ejectLiquid();
    }
    this.liquidState.update(level);
  }

  @Override
  public void onClientTick(Level level, BlockPos pos, BlockState state) {
    this.liquidState.update(level);
  }

  private void ejectLiquid() {
    for (var side : Direction.values()) {
      var pos = this.worldPosition.relative(side);
      if (checkSameFrequency(this.level.getBlockEntity(pos))) {
        continue;
      }

      var fluidHandler =
          this.level.getCapability(Capabilities.FluidHandler.BLOCK, pos, side.getOpposite());
      if (fluidHandler != null) {
        var liquid = getStorage().drain(100, IFluidHandler.FluidAction.SIMULATE);
        if (liquid.getAmount() > 0) {
          int qty = fluidHandler.fill(liquid, IFluidHandler.FluidAction.EXECUTE);
          if (qty > 0) {
            getStorage().drain(qty, IFluidHandler.FluidAction.EXECUTE);
          }
        }
      }
    }
  }

  private boolean checkSameFrequency(BlockEntity blockentity) {
    if (blockentity instanceof BlockEntityDimTank otherTank) {
      return getFrequency().equals(otherTank.getFrequency());
    }
    return false;
  }

  @Override
  public void setFrequency(Frequency frequency) {
    super.setFrequency(frequency);
    if (!this.level.isClientSide) {
      this.liquidState.setFrequency(frequency);
    }
  }

  @Override
  public DimTankStorage getStorage() {
    return (DimTankStorage) DimStorageManager.instance(this.level)
        .getStorage(this.level.registryAccess(), this.getFrequency(), "fluid");
  }

  public int getComparatorInput() {
    int amount = getStorage().getFluidInTank(0).getAmount();
    return amount / 1000;
  }

  public void swapAutoEject() {
    this.autoEject = !this.autoEject;
    this.setChanged();
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    tag.putBoolean("autoEject", this.autoEject);
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    this.liquidState.setFrequency(getFrequency());
    this.autoEject = tag.getBoolean("autoEject");
  }

  @Override
  public ItemInteractionResult useItemOn(ServerPlayer player, Level level, BlockPos pos,
      InteractionHand hand) {
    if (!canAccess(player)) {
      player.displayClientMessage(Component.literal("Access Denied!")
          .withStyle(ChatFormatting.RED), false);
      return super.useItemOn(player, level, pos, hand);
    }

    boolean result = FluidUtil.interactWithFluidHandler(player, hand, getStorage());
    if (!result) {
      return super.useItemOn(player, level, pos, hand);
    }

    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
    return ItemInteractionResult.SUCCESS;
  }

  @Nullable
  public IFluidHandler getFluidHandler(Direction direction) {
    return this.isLocked() ? null : this.getStorage();
  }

  //Synchronizing on block update
  @Override
  public final ClientboundBlockEntityDataPacket getUpdatePacket() {
    CompoundTag root = new CompoundTag();
    root.put("frequency", getFrequency().serializeNBT());
    root.putBoolean("locked", this.isLocked());
    root.putBoolean("autoEject", this.autoEject);
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt,
      HolderLookup.Provider provider) {
    super.onDataPacket(net, pkt, provider);
    CompoundTag tag = pkt.getTag();
    this.setFrequency(Frequency.deserializeNBT(tag.getCompound("frequency")));
    this.setLocked(tag.getBoolean("locked"));
    this.autoEject = tag.getBoolean("autoEject");
  }

  //Synchronizing on chunk load
  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    CompoundTag tag = super.getUpdateTag(registries);
    tag.putBoolean("autoEject", this.autoEject);
    return tag;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
    this.setFrequency(Frequency.deserializeNBT(tag.getCompound("frequency")));
    this.setLocked(tag.getBoolean("locked"));
    this.autoEject = tag.getBoolean("autoEject");
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new DimTankMenu(id, inventory, this, false);
  }

  public class DimTankState extends TankState {

    public DimTankState(Frequency frequency) {
      super(frequency);
    }

    @Override
    public void sendSyncPacket() {
      PacketDistributor.sendToAllPlayers(new SyncLiquidTank(getBlockPos(), this.serverLiquid));
    }

    @Override
    public void onLiquidChanged() {
      level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
      level.getChunkSource().getLightEngine().checkBlock(getBlockPos());
    }
  }
}
