package edivad.dimstorage.menu;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.storage.DimChestStorage;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DimTabletMenu extends AbstractContainerMenu {

  private final DimChestStorage chestInv;

  public DimTabletMenu(int windowId, Inventory inventory, Level level) {
    super(Registration.DIMTABLET_MENU.get(), windowId);

    ItemStack item = inventory.player.getItemInHand(InteractionHand.MAIN_HAND);
    Frequency frequency = new Frequency(item.getOrCreateTag().getCompound("frequency"));

    this.chestInv = (DimChestStorage) DimStorageManager.instance(level.isClientSide)
        .getStorage(frequency, "item");
    this.chestInv.openInventory();

    addOwnSlots();
    addPlayerSlots(inventory);
  }

  private void addOwnSlots() {
    for (int y = 0; y < 6; y++) {
      for (int x = 0; x < 9; x++) {
        this.addSlot(new Slot(chestInv, x + y * 9, 8 + x * 18, 18 + y * 18));
      }
    }
  }

  private void addPlayerSlots(Container playerInventory) {
    // Main Inventory
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 9; x++) {
        this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 140 + y * 18));
      }
    }
    // Hotbar
    for (int x = 0; x < 9; x++) {
      this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 198));
    }
  }

  @Override
  public boolean stillValid(Player player) {
    return true;
  }

  @Override
  public ItemStack quickMoveStack(Player player, int position) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.slots.get(position);

    if (slot != null && slot.hasItem()) {
      ItemStack itemstack1 = slot.getItem();
      itemstack = itemstack1.copy();

      // chest to inventory
      if (position < 54) {
        if (!this.moveItemStackTo(itemstack1, 54, this.slots.size(), true)) {
          return ItemStack.EMPTY;
        }
      }
      // inventory to chest
      else if (!this.moveItemStackTo(itemstack1, 0, 54, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.getCount() == 0) {
        slot.set(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }
    }
    return itemstack;
  }

  @Override
  public void removed(Player player) {
    super.removed(player);
    chestInv.closeInventory();
  }
}
