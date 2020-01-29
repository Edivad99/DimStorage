package edivad.dimstorage.tools.extra.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;

public class FluidUtils {

	public static FluidStack copy(FluidStack liquid, int quantity)
	{
		liquid = liquid.copy();
		liquid.setAmount(quantity);
		return liquid;
	}

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

	//	public static IFluidHandler getFluidHandlerOrEmpty(World world, BlockPos pos, Direction dir)
	//	{
	//		return getFluidHandlerOr(world.getTileEntity(pos), dir, EmptyFluidHandler.INSTANCE);
	//	}
	//
	//	public static IFluidHandler getFluidHandlerOr(TileEntity tile, Direction face, IFluidHandler fluidHandler)
	//	{
	//		IFluidHandler handler = hasFluidHandler(tile, face) ? getFluidHandler_Raw(tile, face) : null;
	//		return handler != null ? handler : fluidHandler;
	//	}
	//
	//	public static boolean hasFluidHandler(TileEntity tile, Direction face)
	//	{
	//		return tile != null && tile.hasCapability(FLUID_HANDLER, face);
	//	}
}
