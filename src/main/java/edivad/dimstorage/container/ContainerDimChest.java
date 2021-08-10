package edivad.dimstorage.container;

import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.storage.DimChestStorage;
import edivad.dimstorage.blockentities.BlockEntityDimChest;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerDimChest extends AbstractContainerMenu {

    private DimChestStorage chestInv;
    public BlockEntityDimChest owner;
    public boolean isOpen;

    public ContainerDimChest(int windowId, Inventory inventory, BlockEntityDimChest owner, boolean isOpen) {
        super(Registration.DIMCHEST_CONTAINER.get(), windowId);
        this.chestInv = owner.getStorage();
        this.owner = owner;
        this.isOpen = isOpen;
        this.chestInv.openInventory();

        addOwnSlots();
        addPlayerSlots(inventory);
    }

    private void addOwnSlots() {
        for(int y = 0; y < 6; y++)
            for(int x = 0; x < 9; x++)
                this.addSlot(new Slot(chestInv, x + y * 9, 8 + x * 18, 18 + y * 18));
    }

    private void addPlayerSlots(Container playerInventory) {
        // Main Inventory
        for(int y = 0; y < 3; y++)
            for(int x = 0; x < 9; x++)
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 140 + y * 18));
        // Hotbar
        for(int x = 0; x < 9; x++)
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 198));
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return chestInv.stillValid(playerIn);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int position) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(position);

        if(slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            // chest to inventory
            if(position < 54) {
                if(!this.moveItemStackTo(itemstack1, 54, this.slots.size(), true))
                    return ItemStack.EMPTY;
            }
            // inventory to chest
            else if(!this.moveItemStackTo(itemstack1, 0, 54, false))
                return ItemStack.EMPTY;

            if(itemstack1.getCount() == 0)
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
        }
        return itemstack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        chestInv.closeInventory();
    }
}
