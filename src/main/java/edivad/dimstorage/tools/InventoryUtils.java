package edivad.dimstorage.tools;

import org.jetbrains.annotations.NotNull;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class InventoryUtils {

  /**
   * Static default implementation for IInventory method
   */
  public static ItemStack removeStackFromSlot(Container inv, int slot) {
    var stack = inv.getItem(slot);
    inv.setItem(slot, ItemStack.EMPTY);
    return stack;
  }

  /**
   * Static default implementation for IInventory method
   */
  @NotNull
  public static ItemStack decrStackSize(Container inv, int slot, int size) {
    var item = inv.getItem(slot);

    if (!item.isEmpty()) {
      if (item.getCount() <= size) {
        inv.setItem(slot, ItemStack.EMPTY);
        inv.setChanged();
        return item;
      }
      var itemstack1 = item.split(size);
      if (item.getCount() == 0) {
        inv.setItem(slot, ItemStack.EMPTY);
      } else {
        inv.setItem(slot, item);
      }

      inv.setChanged();
      return itemstack1;
    }
    return ItemStack.EMPTY;
  }
}
