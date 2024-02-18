package edivad.dimstorage.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class DimStorageMenu extends AbstractContainerMenu {

  protected DimStorageMenu(MenuType<?> menuType, int containerId) {
    super(menuType, containerId);
  }

  protected void addInventorySlots(Container playerInventory) {
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 9; x++) {
        this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 140 + y * 18));
      }
    }
    for (int x = 0; x < 9; x++) {
      this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 198));
    }
  }

  @Override
  public ItemStack quickMoveStack(Player player, int slotIndex) {
    var originalStack = ItemStack.EMPTY;
    var slot = this.slots.get(slotIndex);
    final int numSlots = this.slots.size();
    final int slotsAdded = numSlots - 9 * 4;
    if (slot.hasItem() && slotsAdded > 0) {
      var stackInSlot = slot.getItem();
      originalStack = stackInSlot.copy();
      if (slotIndex < slotsAdded) { // Custom slots to vanilla inventory slots
        if (!this.moveItemStackTo(stackInSlot, slotsAdded, numSlots, false)) {
          return ItemStack.EMPTY;
        }
      } else { // Vanilla inventory slots to custom slots
        if (!this.moveItemStackTo(stackInSlot, 0, slotsAdded, false)) {
          return ItemStack.EMPTY;
        }
      }

      if (stackInSlot.isEmpty()) {
        slot.set(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }
    }
    return originalStack;
  }

  @Override
  public boolean stillValid(Player player) {
    return true;
  }
}
