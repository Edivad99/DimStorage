package edivad.dimstorage.network;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.storage.DimTankStorage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
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

  public void update(Level level) {
    FluidStack prec, succ;
    if (level.isClientSide()) {
      prec = clientLiquid.copy();
      clientLiquid = serverLiquid.copy();
      succ = clientLiquid;
    } else {
      prec = serverLiquid.copy();
      serverLiquid = getFluidStorageServer((ServerLevel) level);
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

  private FluidStack getFluidStorageServer(ServerLevel level) {
    return ((DimTankStorage) DimStorageManager.instance(level)
        .getStorage(frequency, "fluid")).getFluidInTank(0);
  }
}
