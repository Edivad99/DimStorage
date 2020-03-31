package edivad.dimstorage.network;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.storage.DimTankStorage;
import net.minecraft.fluid.Fluids;
import net.minecraftforge.fluids.FluidStack;

public abstract class TankState {

	public Frequency frequency;
	public FluidStack clientLiquid = new FluidStack(Fluids.WATER, 0);
	public FluidStack serverLiquid = new FluidStack(Fluids.WATER, 0);

	public void setFrequency(Frequency frequency)
	{
		this.frequency = frequency;
	}

	public void update(boolean client)
	{
		FluidStack sampleA, sampleB;
		if(client)
		{
			sampleB = clientLiquid.copy();

			clientLiquid = new FluidStack(serverLiquid, serverLiquid.getAmount());
			sampleA = clientLiquid;
		}
		else
		{
			serverLiquid = getFluidStorageServer();
			sampleB = serverLiquid.copy();
			
			//Se aggiungo questa linea funziona tutto
			sendSyncPacket();
//			if(Math.abs(clientLiquid.getAmount() - serverLiquid.getAmount()) > 250)
//			{
//				sendSyncPacket();
//				clientLiquid = serverLiquid;
//			}
			
//			if(!serverLiquid.isFluidEqual(clientLiquid))
//			{
//				sendSyncPacket();
//				clientLiquid = serverLiquid;
//			}
//			else if(Math.abs(clientLiquid.getAmount() - serverLiquid.getAmount()) > 250 || (serverLiquid.getAmount() == 0 && clientLiquid.getAmount() > 0))
//			{
//				// Diff grater than 250 Or server no longer has liquid and client does.
//				sendSyncPacket();
//				clientLiquid = serverLiquid;
//			}

			sampleA = serverLiquid;
		}
		if((sampleB.getAmount() == 0) != (sampleA.getAmount() == 0) || !sampleB.isFluidEqual(sampleA))
		{
			onLiquidChanged();
		}
	}

	public void onLiquidChanged()
	{
	}

	public abstract void sendSyncPacket();

	public void sync(FluidStack liquid)
	{
		serverLiquid = liquid;
	}

	//SERVER SIDE ONLY!
	private FluidStack getFluidStorageServer()
	{
		return ((DimTankStorage) DimStorageManager.instance(false).getStorage(frequency, "fluid")).getFluidInTank(0);
	}
}