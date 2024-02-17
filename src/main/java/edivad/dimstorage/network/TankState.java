package edivad.dimstorage.network;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.storage.DimTankStorage;
import net.neoforged.neoforge.fluids.FluidStack;

public abstract class TankState {

  public FluidStack clientLiquid = FluidStack.EMPTY;
  public FluidStack serverLiquid = FluidStack.EMPTY;
  private Frequency frequency;

  public TankState(Frequency frequency) {
    this.frequency = frequency;
  }

  public void setFrequency(Frequency frequency) {
    this.frequency = frequency;
  }

  public void update(boolean client) {
    FluidStack prec, succ;
    if (client) {
      prec = clientLiquid.copy();
      clientLiquid = serverLiquid.copy();
      succ = clientLiquid;
    } else {
      prec = serverLiquid.copy();
      serverLiquid = getFluidStorageServer();
      succ = serverLiquid;
      sendSyncPacket();
      clientLiquid = serverLiquid.copy();
    }
    if ((prec.getAmount() == 0) != (succ.getAmount() == 0) || !prec.isFluidEqual(succ)) {
      onLiquidChanged();
    }
  }

  public void onLiquidChanged() {
  }

  public abstract void sendSyncPacket();

  public void sync(FluidStack liquid) {
    serverLiquid = liquid;
  }

  //SERVER SIDE ONLY!
  private FluidStack getFluidStorageServer() {
    return ((DimTankStorage) DimStorageManager.instance(false)
        .getStorage(frequency, "fluid")).getFluidInTank(0);
  }
}
