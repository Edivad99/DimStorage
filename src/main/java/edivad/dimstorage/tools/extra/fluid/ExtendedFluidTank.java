package edivad.dimstorage.tools.extra.fluid;

import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class ExtendedFluidTank implements IFluidTank {

	private FluidStack fluid;
	private boolean changeType;
	private int capacity;

	public ExtendedFluidTank(FluidStack type, int capacity)
	{
		if(type == null)
		{
			type = new FluidStack(Fluids.WATER, 1000);
			changeType = true;
		}
		fluid = FluidUtils.copy(type, 0);
		this.capacity = capacity;
	}

	public ExtendedFluidTank(int capacity)
	{
		this(null, capacity);
	}

	@Override
	public FluidStack getFluid()
	{
		return fluid.copy();
	}

	@Override
	public int getCapacity()
	{
		return capacity;
	}

	@Override
	public boolean isFluidValid(FluidStack stack)
	{
		return stack == null || (fluid.getAmount() == 0 && changeType) || fluid.isFluidEqual(stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action)
	{
		if(resource == null || !isFluidValid(resource))
			return 0;

		int toFill = Math.min(getCapacity() - fluid.getAmount(), resource.getAmount());
		if(action == FluidAction.EXECUTE && toFill > 0)
		{
			if(!fluid.isFluidEqual(resource))
				fluid = FluidUtils.copy(resource, fluid.getAmount() + toFill);
			else
				fluid.grow(toFill);
			onLiquidChanged();
		}
		return toFill;
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action)
	{
		if(fluid.getAmount() == 0 || maxDrain <= 0)
			return null;

		int toDrain = Math.min(maxDrain, fluid.getAmount());
		if(action == FluidAction.EXECUTE && toDrain > 0)
		{
			fluid.shrink(toDrain);
			onLiquidChanged();
		}
		return FluidUtils.copy(fluid, toDrain);
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action)
	{
		if(resource == null || !resource.isFluidEqual(fluid))
			return null;

		return drain(resource.getAmount(), action);
	}

	public void onLiquidChanged()
	{
	}

	public void fromTag(CompoundNBT tag)
	{
		fluid = FluidUtils.read(tag);
	}

	public CompoundNBT toTag()
	{
		return FluidUtils.write(fluid, new CompoundNBT());
	}

	@Override
	public int getFluidAmount()
	{
		return fluid.getAmount();
	}
}