package edivad.dimstorage.tools;

import org.jetbrains.annotations.NotNull;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

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

  public static boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex,
      InvWrapper wrapper) {
    boolean flag = false;
    int i = startIndex;

    if (stack.isStackable()) {
      while (!stack.isEmpty() && i < endIndex) {
        var itemstack = wrapper.getStackInSlot(i);
        if (!itemstack.isEmpty() && ItemStack.isSameItem(stack, itemstack)) {
          int j = itemstack.getCount() + stack.getCount();
          int maxSize = stack.getMaxStackSize();
          if (j <= maxSize) {
            stack.setCount(0);
            itemstack.setCount(j);
            flag = true;
          } else if (itemstack.getCount() < maxSize) {
            stack.shrink(maxSize - itemstack.getCount());
            itemstack.setCount(maxSize);
            flag = true;
          }
        }
        i++;
      }
    }

    if (!stack.isEmpty()) {
      i = startIndex;

      while (i < endIndex && !flag) {
        var itemstack1 = wrapper.getStackInSlot(i);
        if (itemstack1.isEmpty() && wrapper.isItemValid(i, stack)) {
          if (stack.getCount() > 64) {
            wrapper.setStackInSlot(i, stack.split(64));
          } else {
            wrapper.setStackInSlot(i, stack.split(stack.getCount()));
          }
          flag = true;
        }
        i++;
      }
    }

    return flag;
  }
}
