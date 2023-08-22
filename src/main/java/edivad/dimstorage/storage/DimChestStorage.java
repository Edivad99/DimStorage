package edivad.dimstorage.storage;

import java.util.Arrays;
import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.OpenChest;
import edivad.dimstorage.tools.InventoryUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

public class DimChestStorage extends AbstractDimStorage implements Container {

  private ItemStack[] items;
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

  public void loadFromTag(CompoundTag tag) {
    empty();
    InventoryUtils.readItemStacksFromTag(items, tag.getList("Items", 10));
  }

  @Override
  public String type() {
    return "item";
  }

  public CompoundTag saveToTag() {
    CompoundTag compound = new CompoundTag();
    compound.put("Items", InventoryUtils.writeItemStacksToTag(this.items));
    return compound;
  }

  public ItemStack getItem(int slot) {
    synchronized (this) {
      return items[slot];
    }
  }

  public ItemStack removeItemNoUpdate(int index) {
    synchronized (this) {
      return InventoryUtils.removeStackFromSlot(this, index);
    }
  }

  public void setItem(int slot, ItemStack stack) {
    synchronized (this) {
      items[slot] = stack;
      setChanged();
    }
  }

  public void openInventory() {
    if (manager.isServer()) {
      synchronized (this) {
        open++;
        if (open >= 1) {
          PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new OpenChest(freq, true));
        }
      }
    }
  }

  public void closeInventory() {
    if (manager.isServer()) {
      synchronized (this) {
        open--;
        if (open <= 0) {
          PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new OpenChest(freq, false));
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
      items = new ItemStack[getContainerSize()];
      Arrays.fill(items, ItemStack.EMPTY);
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
