package edivad.dimstorage.storage;

import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.OpenChest;
import edivad.dimstorage.tools.extra.ArrayUtils;
import edivad.dimstorage.tools.extra.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class DimChestStorage extends AbstractDimStorage implements IInventory {

	private ItemStack[] items;
	private int open;

	public DimChestStorage(DimStorageManager manager, Frequency freq)
	{
		super(manager, freq);
		empty();
	}

	@Override
	public void clearStorage()
	{
		synchronized(this)
		{
			empty();
			setDirty();
		}
	}

	public void loadFromTag(NBTTagCompound tag)
	{
		empty();
		InventoryUtils.readItemStacksFromTag(items, tag.getTagList("Items", 10));
	}

	@Override
	public String type()
	{
		return "item";
	}

	public NBTTagCompound saveToTag()
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("Items", InventoryUtils.writeItemStacksToTag(this.items));
		return compound;
	}

	public ItemStack getStackInSlot(int slot)
	{
		synchronized(this)
		{
			return items[slot];
		}
	}

	public ItemStack removeStackFromSlot(int index)
	{
		synchronized(this)
		{
			return InventoryUtils.removeStackFromSlot(this, index);
		}
	}

	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		synchronized(this)
		{
			items[slot] = stack;
			markDirty();
		}
	}

	public void openInventory()
	{
		if(manager.client)
			return;

		synchronized(this)
		{
			open++;
			if(open == 1)
			{
				PacketHandler.packetReq.sendToAll(new OpenChest(freq, true));
			}
		}
	}

	public void closeInventory()
	{
		if(manager.client)
			return;

		synchronized(this)
		{
			open--;
			if(open == 0)
			{
				PacketHandler.packetReq.sendToAll(new OpenChest(freq, false));
			}
		}
	}

	public int getNumOpen()
	{
		return open;
	}

	@Override
	public int getSizeInventory()
	{
		return 54;
	}

	@Override
	public boolean isEmpty()
	{
		return ArrayUtils.count(items, (stack -> !stack.isEmpty())) <= 0;
	}

	public ItemStack decrStackSize(int slot, int size)
	{
		synchronized(this)
		{
			return InventoryUtils.decrStackSize(this, slot, size);
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void markDirty()
	{
		setDirty();
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return true;
	}

	public void empty()
	{
		synchronized(this)
		{
			items = new ItemStack [getSizeInventory()];
			ArrayUtils.fill(items, ItemStack.EMPTY);
		}
	}

	public void setClientOpen(int i)
	{
		if(manager.client)
			open = i;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}

	@Override
	public String getName()
	{
		return "Dimensional chest";
	}

	@Override
	public boolean hasCustomName()
	{
		return true;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString("Dimensional chest");
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
	}
}
