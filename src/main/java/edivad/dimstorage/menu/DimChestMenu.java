package edivad.dimstorage.menu;

import edivad.dimstorage.blockentities.BlockEntityDimChest;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.storage.DimChestStorage;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class DimChestMenu extends DimStorageMenu {

  private final DimChestStorage chestInv;
  public BlockEntityDimChest owner;
  public boolean isOpen;

  public DimChestMenu(int windowId, Inventory inventory, BlockEntityDimChest owner,
      boolean isOpen) {
    super(Registration.DIMCHEST_MENU.get(), windowId);
    this.chestInv = owner.getStorage();
    this.owner = owner;
    this.isOpen = isOpen;
    this.chestInv.openInventory();

    this.addOwnSlots();
    this.addInventorySlots(inventory);
  }

  private void addOwnSlots() {
    for (int y = 0; y < 6; y++) {
      for (int x = 0; x < 9; x++) {
        this.addSlot(new Slot(chestInv, x + y * 9, 8 + x * 18, 18 + y * 18));
      }
    }
  }

  @Override
  public boolean stillValid(Player player) {
    return chestInv.stillValid(player);
  }

  @Override
  public void removed(Player player) {
    super.removed(player);
    chestInv.closeInventory();
  }
}
