package edivad.dimstorage.blockentities;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.container.ContainerDimTank;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.TankState;
import edivad.dimstorage.network.packet.SyncLiquidTank;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.storage.DimTankStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.network.PacketDistributor;

public class BlockEntityDimTank extends BlockEntityFrequencyOwner {

    public class DimTankState extends TankState {

        @Override
        public void sendSyncPacket() {
            PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new SyncLiquidTank(getBlockPos(), serverLiquid));
        }

        @Override
        public void onLiquidChanged() {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            level.getChunkSource().getLightEngine().checkBlock(getBlockPos());
        }
    }

    public DimTankState liquidState = new DimTankState();
    public boolean autoEject = false;

    //Set the Capability
    private LazyOptional<IFluidHandler> fluidHandler = LazyOptional.empty();

    public BlockEntityDimTank(BlockPos blockPos, BlockState blockState) {
        super(Registration.DIMTANK_TILE.get(), blockPos, blockState);
    }

    @Override
    public void onServerTick(Level level, BlockPos blockPos, BlockState blockState) {
        if(autoEject)
            ejectLiquid();
        liquidState.update(false);
    }

    @Override
    public void onClientTick(Level level, BlockPos blockPos, BlockState blockState) {
        liquidState.update(true);
    }

    private void ejectLiquid() {
        for(Direction side : Direction.values()) {
            BlockEntity tile = level.getBlockEntity(worldPosition.relative(side));
            if(tile != null && checkSameFrequency(tile)) {
                tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()).ifPresent(h -> {
                    FluidStack liquid = getStorage().drain(100, FluidAction.SIMULATE);
                    if(liquid.getAmount() > 0) {
                        int qty = h.fill(liquid, FluidAction.EXECUTE);
                        if(qty > 0) {
                            getStorage().drain(qty, FluidAction.EXECUTE);
                        }
                    }
                });
            }
        }
    }

    private boolean checkSameFrequency(BlockEntity tile) {
        if(tile instanceof BlockEntityDimTank otherTank) {
            return !getFrequency().equals(otherTank.getFrequency());
        }
        return true;
    }

    @Override
    public void setFrequency(Frequency frequency) {
        super.setFrequency(frequency);
        if(!level.isClientSide)
            liquidState.setFrequency(frequency);
        fluidHandler.invalidate();
        fluidHandler = LazyOptional.of(this::getStorage);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        fluidHandler.invalidate();
        fluidHandler = LazyOptional.of(this::getStorage);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        fluidHandler.invalidate();
    }

    @Override
    public DimTankStorage getStorage() {
        return (DimTankStorage) DimStorageManager.instance(level.isClientSide).getStorage(getFrequency(), "fluid");
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
    public InteractionResult activate(Player player, Level level, BlockPos pos, InteractionHand hand) {
        if(!canAccess(player)) {
            player.displayClientMessage(new TextComponent(ChatFormatting.RED + "Access Denied!"), false);
            return super.activate(player, level, pos, hand);
        }

        boolean result = FluidUtil.interactWithFluidHandler(player, hand, getStorage());
        if(!result)
            return super.activate(player, level, pos, hand);

        level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        return InteractionResult.SUCCESS;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if(!locked && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidHandler.cast();
        return super.getCapability(cap, side);
    }

    //Synchronizing on block update
    @Override
    public final ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag root = new CompoundTag();
        root.put("Frequency", getFrequency().serializeNBT());
        root.putBoolean("locked", locked);
        root.putBoolean("autoEject", autoEject);
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        setFrequency(new Frequency(tag.getCompound("Frequency")));
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
        setFrequency(new Frequency(tag.getCompound("Frequency")));
        locked = tag.getBoolean("locked");
        autoEject = tag.getBoolean("autoEject");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player playerEntity) {
        return new ContainerDimTank(id, inventory, this, false);
    }
}
