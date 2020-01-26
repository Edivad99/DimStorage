package edivad.dimstorage.network.packet.tank;

import java.util.function.Supplier;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.network.TankSynchroniser;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

public class UpdateTankLiquid {

	private Frequency freq;
	private FluidStack fluidStack;

	public UpdateTankLiquid(PacketBuffer buf)
	{
		freq = new Frequency(buf.readString(), buf.readInt());
		fluidStack = buf.readFluidStack();
	}

	public UpdateTankLiquid(Frequency freq, FluidStack fluidStack)
	{
		this.freq = freq;
		this.fluidStack = fluidStack;
	}

	public void toBytes(PacketBuffer buf)
	{
		buf.writeString(freq.getOwner());
		buf.writeInt(freq.getChannel());

		buf.writeFluidStack(fluidStack);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			TankSynchroniser.syncClient(freq, fluidStack);
		});
		ctx.get().setPacketHandled(true);
	}

}