package edivad.dimstorage.tile;

import edivad.dimstorage.ModBlocks;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.storage.DimTankStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class TileEntityDimTank extends TileFrequencyOwner {

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
	
	public TankFluidCap fluidCap = new TankFluidCap();
	
	public TileEntityDimTank()
	{
		super(ModBlocks.tileEntityDimTank);
	}

	@Override
	public void tick()
	{
		super.tick();
		ejectLiquid();
	}
	
	private void ejectLiquid()
	{
		for (Direction side : Direction.values())
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
	public boolean activate(PlayerEntity player, World worldIn, BlockPos pos, Hand hand)
	{
		return FluidUtil.interactWithFluidHandler(player, hand, getStorage());
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		// TODO Auto-generated method stub
		return super.getCapability(cap, side);
	}


	@Override
	public DimTankStorage getStorage()
	{
		return (DimTankStorage) DimStorageManager.instance(world.isRemote).getStorage(frequency, "fluid");
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey());
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity)
	{
		return null;
	}
}
