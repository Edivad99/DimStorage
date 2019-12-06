package edivad.dimstorage.tile;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.packet.PacketCustom;
import edivad.dimstorage.Main;
import edivad.dimstorage.client.gui.GuiDimChest;
import edivad.dimstorage.container.ContainerDimChest;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.storage.DimChestStorage;
import edivad.dimstorage.tools.IGuiTile;
import edivad.dimstorage.tools.Translate;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TileEntityDimChest extends TileFrequencyOwner implements IGuiTile {

	private static final float MIN_MOVABLE_POSITION = 0f;
	private static final float MAX_MOVABLE_POSITION = 0.5f;
	private static final float OPENING_SPEED = 0.05f;

	private int openCount;
	public float movablePartState;
	public int rotation;

	public boolean locked;
	private DimChestStorage currentStorage;

	public TileEntityDimChest()
	{
		movablePartState = MIN_MOVABLE_POSITION;
		locked = false;
		//currentStorage = getStorage();
	}

	@Override
	public void update()
	{
		super.update();

		if(!world.isRemote && (world.getTotalWorldTime() % 20 == 0 || openCount != getStorage().getNumOpen()))
		{
			openCount = getStorage().getNumOpen();
			world.addBlockEvent(getPos(), getBlockType(), 1, openCount);
			world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
			//System.out.println(getStorage().freq);
		}
		//		if(world.isRemote && world.getTotalWorldTime() % 20 == 0)
		//			System.out.println(getStorage().freq);

		if(this.openCount > 0)
		{
			if(this.movablePartState < MAX_MOVABLE_POSITION)
				this.movablePartState += OPENING_SPEED;
			else
				this.movablePartState = MAX_MOVABLE_POSITION;
		}
		else
		{
			if(this.movablePartState > MIN_MOVABLE_POSITION)
				this.movablePartState -= OPENING_SPEED;
			else
				this.movablePartState = MIN_MOVABLE_POSITION;
		}
	}

	public void swapLocked()
	{
		locked = !locked;
		this.markDirty();
	}

	@Override
	public boolean receiveClientEvent(int id, int type)
	{
		if(id == 1)
		{
			openCount = type;
			return true;
		}
		return false;
	}

	@Override
	public DimChestStorage getStorage()
	{
		return (DimChestStorage) DimStorageManager.instance(world.isRemote).getStorage(frequency, "item");
	}

	@Override
	public void writeToPacket(MCDataOutput packet)
	{
		super.writeToPacket(packet);
		packet.writeByte(rotation);
	}

	@Override
	public void readFromPacket(MCDataInput packet)
	{
		super.readFromPacket(packet);
		rotation = packet.readUByte() & 3;
	}

	@Override
	public void onPlaced(EntityLivingBase entity)
	{
		rotation = (int) Math.floor(entity.rotationYaw * 4 / 360 + 2.5D) & 3;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setByte("rot", (byte) rotation);
		tag.setBoolean("locked", locked);
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		rotation = tag.getByte("rot") & 3;
		locked = tag.getBoolean("locked");
	}

	@Override
	public boolean activate(EntityPlayer player, World worldIn, BlockPos pos)
	{
		if(canAccess())
		{
			player.openGui(Main.MODID, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		else
		{
			player.sendMessage(new TextComponentString(TextFormatting.RED + Translate.translateToLocal("tile." + Main.MODID + ".accessDenied")));
		}

		return true;
	}

	@Override
	public boolean rotate()
	{
		if(!world.isRemote)
		{
			rotation = (rotation + 1) % 4;
			PacketCustom.sendToChunk(getUpdatePacket(), world, pos.getX() >> 4, pos.getZ() >> 4);
		}
		return true;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return !locked && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(!locked && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return (T) new InvWrapper(getStorage());
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public Container createContainer(EntityPlayer player)
	{
		reloadStorage();
		return new ContainerDimChest(player.inventory, currentStorage);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer createGui(EntityPlayer player)
	{
		reloadStorage();
		currentStorage.empty();
		return new GuiDimChest(player.inventory, currentStorage, this);
	}

	@Override
	public void reloadStorage()
	{
		currentStorage = getStorage();
	}
}
