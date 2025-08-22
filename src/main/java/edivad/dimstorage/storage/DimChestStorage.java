package edivad.dimstorage.storage;

import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.to_client.OpenChest;
import edivad.dimstorage.tools.InventoryUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.network.PacketDistributor;

public class DimChestStorage extends AbstractDimStorage implements Container {

  private NonNullList<ItemStack> items;
  private int open;

  public DimChestStorage(DimStorageManager manager, Frequency freq) {
    super(manager, freq);
    empty();
  }

  @Override
  public void clearStorage() {
    synchronized (this) {
      empty();
      setDirty();
    }
  }

  public void deserialize(ValueInput input) {
    empty();
    ContainerHelper.loadAllItems(input, items);
  }

  @Override
  public String type() {
    return "item";
  }

  public void serialize(ValueOutput output) {
    ContainerHelper.saveAllItems(output, this.items, false);
  }

  public ItemStack getItem(int slot) {
    synchronized (this) {
      return items.get(slot);
    }
  }

  public ItemStack removeItemNoUpdate(int index) {
    synchronized (this) {
      return InventoryUtils.removeStackFromSlot(this, index);
    }
  }

  public void setItem(int slot, ItemStack stack) {
    synchronized (this) {
      items.set(slot, stack);
      setChanged();
    }
  }

  public void openInventory() {
    if (manager.isServer()) {
      synchronized (this) {
        open++;
        if (open >= 1) {
          PacketDistributor.sendToAllPlayers(new OpenChest(freq, true));
        }
      }
    }
  }

  public void closeInventory() {
    if (manager.isServer()) {
      synchronized (this) {
        open--;
        if (open <= 0) {
          PacketDistributor.sendToAllPlayers(new OpenChest(freq, false));
        }
      }
    }
  }

  public int getNumOpen() {
    return open;
  }

  @Override
  public int getContainerSize() {
    return 54;
  }

  @Override
  public boolean isEmpty() {
    for (ItemStack itemStack : items) {
      if (!itemStack.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public ItemStack removeItem(int slot, int size) {
    synchronized (this) {
      return InventoryUtils.decrStackSize(this, slot, size);
    }
  }

  @Override
  public int getMaxStackSize() {
    return 64;
  }

  @Override
  public void setChanged() {
    setDirty();
  }

  @Override
  public boolean stillValid(Player player) {
    return true;
  }

  public void empty() {
    synchronized (this) {
      items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    }
  }

  public void setClientOpen(int i) {
    if (!manager.isServer()) {
      open = i;
    }
  }

  @Override
  public boolean canPlaceItem(int i, ItemStack itemstack) {
    return true;
  }

  @Override
  public void clearContent() {
  }

  @Override
  public void startOpen(Player player) {
  }

  @Override
  public void stopOpen(Player player) {
  }
}
