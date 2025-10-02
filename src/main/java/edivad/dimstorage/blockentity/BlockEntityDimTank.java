package edivad.dimstorage.blockentity;

import org.jetbrains.annotations.Nullable;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.menu.DimTankMenu;
import edivad.dimstorage.network.TankState;
import edivad.dimstorage.network.to_client.SyncLiquidTank;
import edivad.dimstorage.setup.ModRegistration;
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
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

public class BlockEntityDimTank extends BlockEntityFrequencyOwner {

  public DimTankState liquidState;
  public boolean autoEject = false;

  public BlockEntityDimTank(BlockPos pos, BlockState state) {
    super(ModRegistration.DIMTANK_TILE.get(), pos, state);
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
          this.level.getCapability(Capabilities.Fluid.BLOCK, pos, side.getOpposite());
      if (fluidHandler == null) {
        continue;
      }

      ResourceHandlerUtil.move(getStorage(), fluidHandler, __ -> true, 100, null);
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
    if (!this.level.isClientSide()) {
      this.liquidState.setFrequency(frequency);
    }
  }

  @Override
  public DimTankStorage getStorage() {
    return (DimTankStorage) DimStorageManager.instance(this.level)
        .getStorage(this.getFrequency(), "fluid");
  }

  public int getComparatorInput() {
    int amount = getStorage().getAmountAsInt(0);
    return amount / 1000;
  }

  public void swapAutoEject() {
    this.autoEject = !this.autoEject;
    this.setChanged();
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.putBoolean("autoEject", this.autoEject);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    this.liquidState.setFrequency(getFrequency());
    this.autoEject = input.getBooleanOr("autoEject", false);
  }

  @Override
  public InteractionResult useItemOn(ServerPlayer player, Level level, BlockPos pos,
      InteractionHand hand) {
    if (!canAccess(player)) {
      player.displayClientMessage(Component.literal("Access Denied!")
          .withStyle(ChatFormatting.RED), false);
      return super.useItemOn(player, level, pos, hand);
    }

    boolean result = FluidUtil.interactWithFluidHandler(player, hand, pos, getStorage());
    if (!result) {
      return super.useItemOn(player, level, pos, hand);
    }

    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
    return InteractionResult.SUCCESS;
  }

  @Nullable
  public ResourceHandler<FluidResource> getFluidHandler(Direction direction) {
    return this.locked ? null : this.getStorage();
  }

  //Synchronizing on block update
  @Override
  public final ClientboundBlockEntityDataPacket getUpdatePacket() {
    CompoundTag root = new CompoundTag();
    root.store("frequency", Frequency.CODEC, getFrequency());
    root.putBoolean("locked", this.locked);
    root.putBoolean("autoEject", this.autoEject);
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public void onDataPacket(Connection net, ValueInput valueInput) {
    super.onDataPacket(net, valueInput);
    this.setFrequency(valueInput.read("frequency", Frequency.CODEC).orElseThrow());
    this.locked = valueInput.getBooleanOr("locked", false);
    this.autoEject = valueInput.getBooleanOr("autoEject", false);
  }

  //Synchronizing on chunk load
  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    CompoundTag tag = super.getUpdateTag(registries);
    tag.putBoolean("autoEject", this.autoEject);
    return tag;
  }

  @Override
  public void handleUpdateTag(ValueInput input) {
    super.handleUpdateTag(input);
    this.autoEject = input.getBooleanOr("autoEject", false);
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
