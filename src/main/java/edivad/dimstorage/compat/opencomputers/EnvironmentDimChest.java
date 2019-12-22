package edivad.dimstorage.compat.opencomputers;

import edivad.dimstorage.tile.TileEntityDimChest;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import net.minecraft.item.ItemStack;

public class EnvironmentDimChest extends AbstractDimStorageEnviroment<TileEntityDimChest> {

	// wget -f https://raw.githubusercontent.com/Edivad99/DimStorage/master/extra/DimStorageAPI.lua dimstorage

	public EnvironmentDimChest(TileEntityDimChest tile)
	{
		super("dimchest", tile);
	}

	@Callback(doc = "function():string - Returns the owner of DimChest")
	public Object[] getOwner(final Context context, Arguments arguments)
	{
		return new Object [] { tile.frequency.getOwner() };
	}

	@Callback(doc = "function():int - Returns the frequency of DimChest")
	public Object[] getFrequency(final Context context, Arguments arguments)
	{
		return new Object [] { tile.frequency.getChannel() };
	}

	@Callback(doc = "function():boolean - Returns whether DimChest is locked or not")
	public Object[] isLocked(final Context context, Arguments arguments)
	{
		return new Object [] { tile.locked };
	}

	@Callback(doc = "function():boolean - Return if DimChest is public or not")
	public Object[] isPublic(final Context context, Arguments arguments)
	{
		return new Object [] { !tile.frequency.hasOwner() };
	}

	@Callback(doc = "function():boolean - Return if the player can access to DimChest")
	public Object[] canAccess(final Context context, Arguments arguments)
	{
		return new Object [] { tile.canAccess() };
	}

	@Callback(doc = "function():int - Gets the size of inventory in the DimChest")
	public Object[] getSizeInventory(final Context context, Arguments arguments)
	{
		return new Object [] { tile.getStorage().getSizeInventory() };
	}

	@Callback(doc = "function(index:int):table - Gets the stack in the specified slot")
	public Object[] getStackInSlot(final Context context, Arguments arguments)
	{
		if(tile.canAccess())
		{
			int slot = arguments.checkInteger(0);
			if(slot < 0 || slot >= tile.getStorage().getSizeInventory())
				return new Object [] { null, "Invalid slot" };

			ItemStack stack = tile.getStorage().getStackInSlot(slot);

			if(stack.isEmpty())
				return new Object [] { null, "No item" };
			return new Object [] { stack };
		}
		else
		{
			return new Object [] { null, "Access Denied" };
		}
	}

	@Callback(doc = "function():boolean - Toggle the owner, return if the change was successful")
	public Object[] toggleOwner(final Context context, Arguments arguments)
	{
		if(tile.canAccess())
		{
			tile.swapOwner();
			return new Object [] { true };
		}
		else
		{
			return new Object [] { false };
		}
	}

	@Callback(doc = "function(frequency:int):boolean - Change the frequency, return if the change was successful")
	public Object[] changeFrequency(final Context context, Arguments arguments)
	{
		if(tile.canAccess())
		{
			int newFreq = arguments.checkInteger(0);
			if(newFreq < 0 || newFreq >= 1000)
				return new Object [] { false };

			tile.setFreq(tile.frequency.copy().setChannel(newFreq));
			return new Object [] { true };
		}
		else
		{
			return new Object [] { false };
		}
	}

	@Callback(doc = "function():boolean - Toggle the lock, return if the change was successful")
	public Object[] toggleLock(final Context context, Arguments arguments)
	{
		if(tile.canAccess())
		{
			tile.swapLocked();
			return new Object [] { true };
		}
		else
		{
			return new Object [] { false };
		}
	}
}
