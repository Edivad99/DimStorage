package edivad.dimstorage.tile;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.container.ContainerDimTank;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.TankState;
import edivad.dimstorage.network.packet.SyncLiquidTank;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.storage.DimTankStorage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fml.network.PacketDistributor;

public class TileEntityDimTank extends TileFrequencyOwner {

    public class DimTankState extends TankState {

        @Override
        public void sendSyncPacket()
        {
            PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new SyncLiquidTank(getBlockPos(), serverLiquid));
        }

        @Override
        public void onLiquidChanged()
        {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), BlockFlags.DEFAULT);
            level.getChunkSource().getLightEngine().checkBlock(getBlockPos());
        }
    }

    public DimTankState liquidState = new DimTankState();
    public boolean autoEject = false;

    //Set the Capability
    private LazyOptional<IFluidHandler> fluidHandler = LazyOptional.empty();

    public TileEntityDimTank()
    {
        super(Registration.DIMTANK_TILE.get());
    }

    @Override
    public void tick()
    {
        super.tick();
        if(autoEject)
            ejectLiquid();
        liquidState.update(level.isClientSide);
    }

    private void ejectLiquid()
    {
        for(Direction side : Direction.values())
        {
            TileEntity tile = level.getBlockEntity(worldPosition.relative(side));
            if(tile != null && checkSameFrequency(tile))
            {
                tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()).ifPresent(h -> {
                    FluidStack liquid = getStorage().drain(100, FluidAction.SIMULATE);
                    if(liquid.getAmount() > 0)
                    {
                        int qty = h.fill(liquid, FluidAction.EXECUTE);
                        if(qty > 0)
                        {
                            getStorage().drain(qty, FluidAction.EXECUTE);
                        }
                    }
                });
            }
        }
    }

    private boolean checkSameFrequency(TileEntity tile)
    {
        if(tile instanceof TileEntityDimTank)
        {
            TileEntityDimTank otherTank = (TileEntityDimTank) tile;

            return !getFrequency().equals(otherTank.getFrequency());
        }
        return true;
    }

    @Override
    public void setFrequency(Frequency frequency)
    {
        super.setFrequency(frequency);
        if(!level.isClientSide)
            liquidState.setFrequency(frequency);
        fluidHandler.invalidate();
        fluidHandler = LazyOptional.of(this::getStorage);
    }

    @Override
    public void setLevelAndPosition(World world, BlockPos pos)
    {
        super.setLevelAndPosition(world, pos);
        fluidHandler.invalidate();
        fluidHandler = LazyOptional.of(this::getStorage);
    }

    @Override
    public void setRemoved()
    {
        super.setRemoved();
        fluidHandler.invalidate();
    }

    @Override
    public DimTankStorage getStorage()
    {
        return (DimTankStorage) DimStorageManager.instance(level.isClientSide).getStorage(getFrequency(), "fluid");
    }

    public int getComparatorInput()
    {
        int amount = getStorage().getFluidInTank(0).getAmount();
        return amount / 1000;
    }

    public void swapAutoEject()
    {
        autoEject = !autoEject;
        this.setChanged();
    }

    @Override
    public CompoundNBT save(CompoundNBT tag)
    {
        super.save(tag);
        tag.putBoolean("autoEject", autoEject);
        return tag;
    }

    @Override
    public void load(BlockState state, CompoundNBT tag)
    {
        super.load(state, tag);
        liquidState.setFrequency(getFrequency());
        autoEject = tag.getBoolean("autoEject");
    }

    @Override
    public ActionResultType activate(PlayerEntity player, World worldIn, BlockPos pos, Hand hand)
    {
        boolean result = FluidUtil.interactWithFluidHandler(player, hand, getStorage());
        if(!result)
            return super.activate(player, worldIn, pos, hand);
        worldIn.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        return ActionResultType.SUCCESS;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        if(!locked && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidHandler.cast();
        return super.getCapability(cap, side);
    }

    //Synchronizing on block update
    @Override
    public final SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT root = new CompoundNBT();
        root.put("Frequency", getFrequency().serializeNBT());
        root.putBoolean("locked", locked);
        root.putBoolean("autoEject", autoEject);
        return new SUpdateTileEntityPacket(getBlockPos(), 1, root);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        CompoundNBT tag = pkt.getTag();
        setFrequency(new Frequency(tag.getCompound("Frequency")));
        locked = tag.getBoolean("locked");
        autoEject = tag.getBoolean("autoEject");
    }

    //Synchronizing on chunk load
    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT tag = super.getUpdateTag();
        tag.putBoolean("autoEject", autoEject);
        return tag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag)
    {
        setFrequency(new Frequency(tag.getCompound("Frequency")));
        locked = tag.getBoolean("locked");
        autoEject = tag.getBoolean("autoEject");
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity)
    {
        return new ContainerDimTank(id, playerInventory, this, false);
    }
}
