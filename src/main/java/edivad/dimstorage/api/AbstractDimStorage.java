package edivad.dimstorage.api;

import edivad.dimstorage.manager.DimStorageManager;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public abstract class AbstractDimStorage implements ValueIOSerializable {

  public final DimStorageManager manager;
  public final Frequency freq;
  private boolean dirty;
  private int changeCount;

  public AbstractDimStorage(DimStorageManager manager, Frequency freq) {
    this.manager = manager;
    this.freq = freq;

    this.dirty = false;
    this.changeCount = 0;
  }

  public void setDirty() {
    if (manager.isServer()) {
      if (!dirty) {
        dirty = true;
        manager.requestSave(this);
      }

      changeCount++;
    }
  }

  public void setClean() {
    dirty = false;
  }

  public int getChangeCount() {
    return changeCount;
  }

  public abstract void clearStorage();

  public abstract String type();

  @Override
  public String toString() {
    return freq + ",type=" + type();
  }
}
