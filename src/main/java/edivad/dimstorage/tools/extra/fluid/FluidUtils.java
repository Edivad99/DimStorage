package edivad.dimstorage.tools.extra.fluid;

import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;

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
