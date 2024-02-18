package edivad.dimstorage.menu;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.storage.DimChestStorage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;

public class DimTabletMenu extends DimStorageMenu {

  private final DimChestStorage chestInv;

  public DimTabletMenu(int windowId, Inventory inventory, Level level) {
    super(Registration.DIMTABLET_MENU.get(), windowId);

    var item = inventory.player.getItemInHand(InteractionHand.MAIN_HAND);
    var frequency = new Frequency(item.getOrCreateTag().getCompound("frequency"));

    this.chestInv = (DimChestStorage) DimStorageManager.instance(level)
        .getStorage(frequency, "item");
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
  public void removed(Player player) {
    super.removed(player);
    chestInv.closeInventory();
  }
}
