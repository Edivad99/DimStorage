package edivad.dimstorage.blocks;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.tile.TileFrequencyOwner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class DimBlockBase extends Block {

	public DimBlockBase(Properties properties)
	{
		super(properties);
	}

	@Override
	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return false;
	}

	@Override
	public boolean isSolid(BlockState state)
	{
		return false;
	}

	@Override
	public boolean isVariableOpacity()
	{
		return false;
	}

	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileFrequencyOwner)
		{
			TileFrequencyOwner block = (TileFrequencyOwner) tile;
			if(block.canAccess(player) || player.isCreative())
				return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
		}
		return false;
	}

	@Override
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack)
	{
		TileFrequencyOwner tile = (TileFrequencyOwner) worldIn.getTileEntity(pos);
		if(tile != null)
		{
			ItemStack item = createItem(tile.frequency);

			float xOffset = worldIn.rand.nextFloat() * 0.8F + 0.1F;
			float yOffset = worldIn.rand.nextFloat() * 0.8F + 0.1F;
			float zOffset = worldIn.rand.nextFloat() * 0.8F + 0.1F;

			ItemEntity entityitem = new ItemEntity(worldIn, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, item);
			worldIn.addEntity(entityitem);
		}

		worldIn.removeTileEntity(pos);
		worldIn.removeBlock(pos, false);
	}

	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		TileFrequencyOwner tile = (TileFrequencyOwner) world.getTileEntity(pos);
		return createItem(tile.frequency);
	}

	private ItemStack createItem(Frequency freq)
	{
		ItemStack stack = new ItemStack(this, 1);
		if(!stack.hasTag())
		{
			stack.setTag(new CompoundNBT());
		}

		return freq.writeToStack(stack);
	}
}
