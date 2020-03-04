package edivad.dimstorage.tile;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.container.ContainerDimTank;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.TankSynchroniser;
import edivad.dimstorage.network.packet.tank.SyncLiquidTank;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.storage.DimTankStorage;
import edivad.dimstorage.tools.extra.fluid.FluidUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;

public class TileEntityDimTank extends TileFrequencyOwner {

	public class DimTankState extends TankSynchroniser.TankState {

		@Override
		public void sendSyncPacket()
		{
			PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunk(pos.getX(), pos.getZ())), new SyncLiquidTank(getPos(), s_liquid));
		}

		@Override
		public void onLiquidChanged()
		{
			//world.checkLight(pos);
		}
	}

	public class TankFluidCap implements IFluidHandler {

		@Override
		public int getTanks()
		{
			return 1;
		}

		@Override
		public FluidStack getFluidInTank(int tank)
		{
			return getStorage().getFluidInTank(tank);
		}

		@Override
		public int getTankCapacity(int tank)
		{
			return DimTankStorage.CAPACITY;
		}

		@Override
		public boolean isFluidValid(int tank, FluidStack stack)
		{
			return getStorage().isFluidValid(tank, stack);
		}

		@Override
		public int fill(FluidStack resource, FluidAction action)
		{
			return getStorage().fill(resource, action);
		}

		@Override
		public FluidStack drain(FluidStack resource, FluidAction action)
		{
			return getStorage().drain(resource, action);
		}

		@Override
		public FluidStack drain(int maxDrain, FluidAction action)
		{
			return getStorage().drain(maxDrain, action);
		}
	}

	public DimTankState liquidState = new DimTankState();
	public TankFluidCap fluidCap = new TankFluidCap();

	public TileEntityDimTank()
	{
		super(Registration.DIMTANK_TILE.get());
	}

	@Override
	public void tick()
	{
		super.tick();
		ejectLiquid();
		liquidState.update(world.isRemote);
	}

	private void ejectLiquid()
	{
		for(Direction side : Direction.values())
		{
			TileEntity tile = world.getTileEntity(pos.offset(side));
			if(tile != null)
			{
				IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()).orElse(null);
				if(handler != null)
				{
					FluidStack liquid = getStorage().drain(100, FluidAction.SIMULATE);
					if(liquid != null)
					{
						int qty = handler.fill(liquid, FluidAction.EXECUTE);
						if(qty > 0)
						{
							getStorage().drain(qty, FluidAction.EXECUTE);
						}
					}
				}
			}
		}
	}

	@Override
	public void setFreq(Frequency frequency)
	{
		super.setFreq(frequency);
		if(!world.isRemote)
			liquidState.setFrequency(frequency);
	}

	@Override
	public DimTankStorage getStorage()
	{
		return (DimTankStorage) DimStorageManager.instance(world.isRemote).getStorage(frequency, "fluid");
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		super.write(tag);
		return tag;
	}

	@Override
	public void read(CompoundNBT tag)
	{
		super.read(tag);
		liquidState.setFrequency(frequency);
	}

	public int getLightValue()
	{
		if(liquidState.s_liquid.getAmount() > 0)
			return FluidUtils.getLuminosity(liquidState.c_liquid, liquidState.s_liquid.getAmount() / 16D);
		return 0;
	}

	@Override
	public boolean activate(PlayerEntity player, World worldIn, BlockPos pos, Hand hand)
	{
		if(player.isSneaking())
		{
			if(canAccess(player))
			{
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) this, buf -> buf.writeBlockPos(getPos()).writeBoolean(false));
			}
			else
			{
				player.sendMessage(new StringTextComponent(TextFormatting.RED + "Access Denied!"));
			}
			return true;
		}
		else
			return FluidUtil.interactWithFluidHandler(player, hand, getStorage());
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		if(!locked && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return LazyOptional.of(() -> fluidCap).cast();
		}
		return super.getCapability(cap, side);
	}

	//Synchronizing on block update
	@Override
	public final SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT root = new CompoundNBT();
		root.put("Frequency", frequency.writeToNBT(new CompoundNBT()));
		root.putBoolean("locked", locked);
		return new SUpdateTileEntityPacket(getPos(), 1, root);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		CompoundNBT tag = pkt.getNbtCompound();
		frequency.set(new Frequency(tag.getCompound("Frequency")));
		locked = tag.getBoolean("locked");
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
