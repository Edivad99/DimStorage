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
            PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new SyncLiquidTank(getPos(), serverLiquid));
        }

        @Override
        public void onLiquidChanged()
        {
            world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), BlockFlags.DEFAULT);
            world.getChunkProvider().getLightManager().checkBlock(getPos());
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
        liquidState.update(world.isRemote);
    }

    private void ejectLiquid()
    {
        for(Direction side : Direction.values())
        {
            TileEntity tile = world.getTileEntity(pos.offset(side));
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
        if(!world.isRemote)
            liquidState.setFrequency(frequency);
        fluidHandler.invalidate();
        fluidHandler = LazyOptional.of(this::getStorage);
    }

    @Override
    public void remove()
    {
        super.remove();
        fluidHandler.invalidate();
    }

    @Override
    public DimTankStorage getStorage()
    {
        return (DimTankStorage) DimStorageManager.instance(world.isRemote).getStorage(getFrequency(), "fluid");
    }

    public int getComparatorInput()
    {
        int amount = getStorage().getFluidInTank(0).getAmount();
        return amount / 1000;
    }

    public void swapAutoEject()
    {
        autoEject = !autoEject;
        this.markDirty();
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        super.write(tag);
        tag.putBoolean("autoEject", autoEject);
        return tag;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag)
    {
        super.read(state, tag);
        liquidState.setFrequency(getFrequency());
        autoEject = tag.getBoolean("autoEject");
    }

    @Override
    public ActionResultType activate(PlayerEntity player, World worldIn, BlockPos pos, Hand hand)
    {
        boolean result = FluidUtil.interactWithFluidHandler(player, hand, getStorage());
        if(!result)
            return super.activate(player, worldIn, pos, hand);
        worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        return ActionResultType.SUCCESS;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        if(!locked && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            if(!fluidHandler.isPresent())
                fluidHandler = LazyOptional.of(this::getStorage);
            return fluidHandler.cast();
        }
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
        return new SUpdateTileEntityPacket(getPos(), 1, root);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        CompoundNBT tag = pkt.getNbtCompound();
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
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey());
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity)
    {
        return new ContainerDimTank(id, playerInventory, this, false);
    }
}
