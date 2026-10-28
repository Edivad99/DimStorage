package edivad.dimstorage.menu;

import edivad.dimstorage.blockentity.BlockEntityDimTank;
import edivad.dimstorage.setup.ModRegistration;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class DimTankMenu extends DimStorageMenu {

  public BlockEntityDimTank owner;
  public boolean isOpen;

  public DimTankMenu(int windowId, Inventory inventory, BlockEntityDimTank owner,
      boolean isOpen) {
    super(ModRegistration.DIMTANK_MENU.get(), windowId);
    this.owner = owner;
    this.isOpen = isOpen;

    this.addInventorySlots(inventory);
  }

  @Override
  public boolean stillValid(Player player) {
    return owner.stillValid(player);
  }
}
