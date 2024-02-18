package edivad.dimstorage.blockentities;

import org.jetbrains.annotations.Nullable;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.menu.DimTankMenu;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.TankState;
import edivad.dimstorage.network.to_client.SyncLiquidTank;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.storage.DimTankStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class BlockEntityDimTank extends BlockEntityFrequencyOwner {

  public DimTankState liquidState;
  public boolean autoEject = false;

  public BlockEntityDimTank(BlockPos pos, BlockState state) {
    super(Registration.DIMTANK_TILE.get(), pos, state);
    liquidState = new DimTankState(getFrequency());
  }

  @Override
  public void onServerTick(Level level, BlockPos pos, BlockState state) {
    if (autoEject) {
      ejectLiquid();
    }
    liquidState.update(level);
  }

  @Override
  public void onClientTick(Level level, BlockPos pos, BlockState state) {
    liquidState.update(level);
  }

  private void ejectLiquid() {
    for (var side : Direction.values()) {
      var pos = worldPosition.relative(side);
      if (checkSameFrequency(level.getBlockEntity(pos))) {
        continue;
      }

      var fluidHandler =
          level.getCapability(Capabilities.FluidHandler.BLOCK, pos, side.getOpposite());
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
    if (!level.isClientSide) {
      liquidState.setFrequency(frequency);
    }
  }

  @Override
  public DimTankStorage getStorage() {
    return (DimTankStorage) DimStorageManager.instance(level)
        .getStorage(getFrequency(), "fluid");
  }

  public int getComparatorInput() {
    int amount = getStorage().getFluidInTank(0).getAmount();
    return amount / 1000;
  }

  public void swapAutoEject() {
    autoEject = !autoEject;
    this.setChanged();
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putBoolean("autoEject", autoEject);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    liquidState.setFrequency(getFrequency());
    autoEject = tag.getBoolean("autoEject");
  }

  @Override
  public InteractionResult activate(ServerPlayer player, Level level, BlockPos pos,
      InteractionHand hand) {
    if (!canAccess(player)) {
      player.displayClientMessage(Component.literal("Access Denied!")
          .withStyle(ChatFormatting.RED), false);
      return super.activate(player, level, pos, hand);
    }

    boolean result = FluidUtil.interactWithFluidHandler(player, hand, getStorage());
    if (!result) {
      return super.activate(player, level, pos, hand);
    }

    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
    return InteractionResult.SUCCESS;
  }

  @Nullable
  public IFluidHandler getFluidHandler(Direction direction) {
    return locked ? null : this.getStorage();
  }

  //Synchronizing on block update
  @Override
  public final ClientboundBlockEntityDataPacket getUpdatePacket() {
    CompoundTag root = new CompoundTag();
    root.put("frequency", getFrequency().serializeNBT());
    root.putBoolean("locked", locked);
    root.putBoolean("autoEject", autoEject);
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    CompoundTag tag = pkt.getTag();
    setFrequency(new Frequency(tag.getCompound("frequency")));
    locked = tag.getBoolean("locked");
    autoEject = tag.getBoolean("autoEject");
  }

  //Synchronizing on chunk load
  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    tag.putBoolean("autoEject", autoEject);
    return tag;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    setFrequency(new Frequency(tag.getCompound("frequency")));
    locked = tag.getBoolean("locked");
    autoEject = tag.getBoolean("autoEject");
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
      PacketHandler.sendToAll(new SyncLiquidTank(getBlockPos(), serverLiquid));
    }

    @Override
    public void onLiquidChanged() {
      level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
      level.getChunkSource().getLightEngine().checkBlock(getBlockPos());
    }
  }
}
