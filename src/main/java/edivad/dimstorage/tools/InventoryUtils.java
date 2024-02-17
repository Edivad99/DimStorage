package edivad.dimstorage.tools;

import org.jetbrains.annotations.NotNull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class InventoryUtils {

  /**
   * NBT item loading function with support for stack sizes > 32K
   */
  public static void readItemStacksFromTag(ItemStack[] items, ListTag tagList) {
    for (int i = 0; i < tagList.size(); i++) {
      var tag = tagList.getCompound(i);
      int b = tag.getShort("Slot");
      items[b] = ItemStack.of(tag);
      var quant = tag.get("Quantity");
      if (quant instanceof NumericTag numericTag) {
        items[b].setCount(numericTag.getAsInt());
      }
    }
  }

  /**
   * NBT item saving function
   */
  public static ListTag writeItemStacksToTag(ItemStack[] items) {
    return writeItemStacksToTag(items, 64);
  }

  /**
   * NBT item saving function with support for stack sizes > 32K
   */
  public static ListTag writeItemStacksToTag(ItemStack[] items, int maxQuantity) {
    ListTag tagList = new ListTag();
    for (int i = 0; i < items.length; i++) {
      var tag = new CompoundTag();
      tag.putShort("Slot", (short) i);
      items[i].save(tag);

      if (maxQuantity > Short.MAX_VALUE) {
        tag.putInt("Quantity", items[i].getCount());
      } else if (maxQuantity > Byte.MAX_VALUE) {
        tag.putShort("Quantity", (short) items[i].getCount());
      }

      tagList.add(tag);
    }
    return tagList;
  }

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
