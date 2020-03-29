package edivad.dimstorage.network.packet.tank;

import java.util.function.Supplier;

import edivad.dimstorage.Main;
import edivad.dimstorage.tile.TileEntityDimTank;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncLiquidTank {

	private BlockPos pos;
	private FluidStack fluidStack;

	public SyncLiquidTank(PacketBuffer buf)
	{
		pos = buf.readBlockPos();
		fluidStack = buf.readFluidStack();
	}

	public SyncLiquidTank(BlockPos pos, FluidStack fluidStack)
	{
		this.pos = pos;
		this.fluidStack = fluidStack;
	}

	public void toBytes(PacketBuffer buf)
	{
		buf.writeBlockPos(pos);
		buf.writeFluidStack(fluidStack);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			World world = Main.proxy.getClientWorld();
			if(world.isBlockPresent(pos))
			{
				TileEntity te = world.getTileEntity(pos);
				if(te instanceof TileEntityDimTank)
				{
					TileEntityDimTank tank = (TileEntityDimTank) te;
					tank.liquidState.sync(fluidStack);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
