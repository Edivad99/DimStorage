package edivad.dimstorage.tile;

import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.Frequency;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileFrequencyOwner extends TileEntity implements ITickable {

	public Frequency frequency = new Frequency();
	private int changeCount;

	public void setFreq(Frequency frequency)
	{
		this.frequency = frequency;
		markDirty();
		IBlockState state = world.getBlockState(pos);
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, state, state, 3);
	}

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

	//Synchronizing on chunk load
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound tag = super.getUpdateTag();
		tag.setTag("Frequency", frequency.writeToNBT(tag));
		return tag;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		frequency.set(new Frequency(tag.getCompoundTag("Frequency")));
	}
}
