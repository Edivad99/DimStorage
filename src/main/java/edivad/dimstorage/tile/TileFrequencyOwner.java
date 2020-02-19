package edivad.dimstorage.tile;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.Frequency;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class TileFrequencyOwner extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	public TileFrequencyOwner(TileEntityType<?> tileEntityTypeIn)
	{
		super(tileEntityTypeIn);
	}

	public Frequency frequency = new Frequency();
	private int changeCount;

	public void setFreq(Frequency frequency)
	{
		this.frequency = frequency;
		markDirty();
		BlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
	}

	@OnlyIn(Dist.CLIENT)
	public void swapOwner()
	{
		if(frequency.hasOwner())
			setFreq(frequency.copy().setOwner("public"));
		else
			setFreq(frequency.copy().setOwner(Main.proxy.getClientPlayer().getDisplayName().getFormattedText()));
	}

	public boolean canAccess(PlayerEntity player)
	{
		return frequency.canAccess(player);
	}

	@Override
	public void tick()
	{
		if(getStorage().getChangeCount() > changeCount)
		{
			world.updateComparatorOutputLevel(pos, this.getBlockState().getBlock());
			changeCount = getStorage().getChangeCount();
		}
	}

	public abstract AbstractDimStorage getStorage();

	@Override
	public void read(CompoundNBT tag)
	{
		super.read(tag);
		frequency.set(new Frequency(tag.getCompound("Frequency")));
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		super.write(tag);
		tag.put("Frequency", frequency.writeToNBT(new CompoundNBT()));
		return tag;
	}

	public ActionResultType activate(PlayerEntity player, World worldIn, BlockPos pos)
	{
		return ActionResultType.FAIL;
	}

	public void onPlaced(LivingEntity entity)
	{
	}

	//Synchronizing on chunk load
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT tag = super.getUpdateTag();
		tag.put("Frequency", frequency.writeToNBT(new CompoundNBT()));
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag)
	{
		frequency.set(new Frequency(tag.getCompound("Frequency")));
	}
}
