package edivad.dimstorage.storage;

import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class DimTankStorage extends AbstractDimStorage implements IFluidHandler {

  public static final int CAPACITY = 16000;
  private Tank tank;

  public DimTankStorage(DimStorageManager manager, Frequency freq) {
    super(manager, freq);
    tank = new Tank(CAPACITY);
  }

  @Override
  public int getTanks() {
    return 1;
  }

  @Override
  public FluidStack getFluidInTank(int tank) {
    return this.tank.getFluid().copy();
  }

  @Override
  public int getTankCapacity(int tank) {
    return CAPACITY;
  }

  @Override
  public boolean isFluidValid(int tank, FluidStack stack) {
    return this.tank.isFluidValid(stack);
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    return tank.fill(resource, action);
  }

  @Override
  public FluidStack drain(FluidStack resource, FluidAction action) {
    return tank.drain(resource, action);
  }

  @Override
  public FluidStack drain(int maxDrain, FluidAction action) {
    return tank.drain(maxDrain, action);
  }

  @Override
  public void clearStorage() {
    tank = new Tank(CAPACITY);
    setDirty();
  }

  @Override
  public String type() {
    return "fluid";
  }

  @Override
  public CompoundTag saveToTag() {
    CompoundTag compound = new CompoundTag();
    compound.put("tank", tank.writeToNBT(new CompoundTag()));
    return compound;
  }

  @Override
  public void loadFromTag(CompoundTag tag) {
    tank.readFromNBT(tag.getCompound("tank"));
  }

  private class Tank extends FluidTank {

    public Tank(int capacity) {
      super(capacity);
    }

    @Override
    protected void onContentsChanged() {
      setDirty();
    }
  }
}
