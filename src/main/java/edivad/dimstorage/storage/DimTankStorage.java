package edivad.dimstorage.storage;

import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class DimTankStorage extends AbstractDimStorage implements ResourceHandler<FluidResource> {

  public static final int CAPACITY = 16000;
  private Tank tank;

  public DimTankStorage(DimStorageManager manager, Frequency freq) {
    super(manager, freq);
    tank = new Tank(CAPACITY);
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public FluidResource getResource(int i) {
    return tank.getResource(i);
  }

  @Override
  public long getAmountAsLong(int i) {
    return tank.getAmountAsLong(i);
  }

  @Override
  public long getCapacityAsLong(int i, FluidResource resource) {
    return tank.getCapacityAsLong(i, resource);
  }

  @Override
  public boolean isValid(int i, FluidResource resource) {
    return tank.isValid(i, resource);
  }

  @Override
  public int insert(int i, FluidResource resource, int amount, TransactionContext ctx) {
    return tank.insert(i, resource, amount, ctx);
  }

  @Override
  public int extract(int i, FluidResource resource, int amount, TransactionContext ctx) {
    return tank.extract(i, resource, amount, ctx);
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
  public void serialize(ValueOutput output) {
    tank.serialize(output);
  }

  @Override
  public void deserialize(ValueInput input) {
    tank.deserialize(input);
  }

  private class Tank extends FluidStacksResourceHandler {

    public Tank(int capacity) {
      super(1, capacity);
    }

    @Override
    protected void onContentsChanged(int index, FluidStack previousContents) {
      setDirty();
    }
  }
}
