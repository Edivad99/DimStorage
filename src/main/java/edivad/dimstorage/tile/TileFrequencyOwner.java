package edivad.dimstorage.tile;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.packet.ICustomPacketTile;
import codechicken.lib.packet.PacketCustom;
import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.network.DimStorageCPH;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileFrequencyOwner extends TileEntity implements ITickable, ICustomPacketTile {

	public Frequency frequency = new Frequency();
	private int changeCount;

	public void setFreq(Frequency frequency)
	{
		this.frequency = frequency;
		markDirty();
		IBlockState state = world.getBlockState(pos);
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, state, state, 3);
		if(!world.isRemote)
		{
			sendUpdatePacket();
		}
		reloadStorage();
	}

	public abstract void reloadStorage();

	public void swapOwner()
	{
		if(frequency.hasOwner())
			setFreq(frequency.copy().setOwner("public"));
		else
			setFreq(frequency.copy().setOwner(Minecraft.getMinecraft().player.getDisplayNameString()));
	}

	public boolean canAccess()
	{
		if(!frequency.hasOwner())
			return true;
		return frequency.getOwner().equals(Minecraft.getMinecraft().player.getDisplayNameString());
	}

	@Override
	public void update()
	{
		if(getStorage().getChangeCount() > changeCount)
		{
			world.updateComparatorOutputLevel(pos, getBlockType());
			changeCount = getStorage().getChangeCount();
		}
	}

	public abstract AbstractDimStorage getStorage();

	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		frequency.set(new Frequency(tag.getCompoundTag("Frequency")));
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setTag("Frequency", frequency.writeToNBT(new NBTTagCompound()));
		return tag;
	}

	public boolean activate(EntityPlayer player, World worldIn, BlockPos pos)
	{
		return false;
	}

	public void onPlaced(EntityLivingBase entity)
	{
	}

	protected void sendUpdatePacket()
	{
		createPacket().sendToChunk(world, getPos().getX() >> 4, getPos().getZ() >> 4);
	}

	public PacketCustom createPacket()
	{
		PacketCustom packet = new PacketCustom(DimStorageCPH.channel, 1);
		writeToPacket(packet);
		return packet;
	}

	@Override
	public final SPacketUpdateTileEntity getUpdatePacket()
	{
		return createPacket().toTilePacket(getPos());
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return createPacket().toNBTTag(super.getUpdateTag());
	}

	public void writeToPacket(MCDataOutput packet)
	{
		frequency.writeToPacket(packet);
	}

	public void readFromPacket(MCDataInput packet)
	{
		frequency.set(Frequency.readFromPacket(packet));
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromPacket(PacketCustom.fromTilePacket(pkt));
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		readFromPacket(PacketCustom.fromNBTTag(tag));
	}

	public boolean rotate()
	{
		return false;
	}
}
