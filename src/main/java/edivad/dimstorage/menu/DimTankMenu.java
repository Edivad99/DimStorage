package edivad.dimstorage.menu;

import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.setup.Registration;
import net.minecraft.world.entity.player.Inventory;

public class DimTankMenu extends DimStorageMenu {

  public BlockEntityDimTank owner;
  public boolean isOpen;

  public DimTankMenu(int windowId, Inventory inventory, BlockEntityDimTank owner,
      boolean isOpen) {
    super(Registration.DIMTANK_MENU.get(), windowId);
    this.owner = owner;
    this.isOpen = isOpen;

    this.addInventorySlots(inventory);
  }
}
