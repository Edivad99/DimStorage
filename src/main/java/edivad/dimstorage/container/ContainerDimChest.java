package edivad.dimstorage.container;

import edivad.dimstorage.storage.DimChestStorage;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

@ChestContainer()
public class ContainerDimChest extends Container {

	public DimChestStorage chestInv;

	public ContainerDimChest(InventoryPlayer playerInventory, DimChestStorage chestInv)
	{
		this.chestInv = chestInv;
		this.chestInv.openInventory();

		addOwnSlots();
		addPlayerSlots(playerInventory);
	}

	private void addOwnSlots()
	{
		for(int y = 0; y < 6; y++)
			for(int x = 0; x < 9; x++)
				this.addSlotToContainer(new Slot(chestInv, x + y * 9, 8 + x * 18, 18 + y * 18));
	}

	private void addPlayerSlots(IInventory playerInventory)
	{
		// Main Inventory
		for(int y = 0; y < 3; y++)
			for(int x = 0; x < 9; x++)
				this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 140 + y * 18));
		// Hotbar
		for(int x = 0; x < 9; x++)
			this.addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 198));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return chestInv.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int position)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(position);

		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			// chest to inventory
			if(position < 54)
			{
				if(!this.mergeItemStack(itemstack1, 54, this.inventorySlots.size(), true))
					return ItemStack.EMPTY;
			}
			// inventory to chest
			else if(!this.mergeItemStack(itemstack1, 0, 54, false))
				return ItemStack.EMPTY;

			if(itemstack1.getCount() == 0)
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
		}
		return itemstack;
	}

	@Override
	public void onContainerClosed(EntityPlayer entityplayer)
	{
		super.onContainerClosed(entityplayer);
		chestInv.closeInventory();
	}
}
