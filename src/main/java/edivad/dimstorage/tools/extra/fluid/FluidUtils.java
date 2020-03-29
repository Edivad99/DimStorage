package edivad.dimstorage.tools.extra.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;

public class FluidUtils {

	public static FluidStack read(CompoundNBT tag)
	{
		FluidStack stack = FluidStack.loadFluidStackFromNBT(tag);
		return stack == null ? new FluidStack(Fluids.WATER, 0) : stack;
	}

	public static CompoundNBT write(FluidStack fluid, CompoundNBT tag)
	{
		return fluid == null || fluid.getFluid() == null ? tag : fluid.writeToNBT(tag);
	}

	public static int getLuminosity(FluidStack stack, double density)
	{
		Fluid fluid = stack.getFluid();
		if(fluid == null)
		{
			return 0;
		}
		int light = fluid.getAttributes().getLuminosity(stack);
		if(fluid.getAttributes().isGaseous())
		{
			light = (int) (light * density);
		}
		return light;
	}
}
