package edivad.dimstorage.blocks;

import javax.annotation.Nullable;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.compat.top.TOPInfoProvider;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tile.TileFrequencyOwner;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class DimChest extends Block implements TOPInfoProvider {

	public static final ResourceLocation DIMCHEST = new ResourceLocation(Main.MODID, "dimensional_chest");

	public DimChest()
	{
		super(Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(20F, 100F));
		setRegistryName(DIMCHEST);
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileEntityDimChest();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.INVISIBLE;
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
		if(tile instanceof TileEntityDimChest)
		{
			TileEntityDimChest chest = (TileEntityDimChest) tile;
			if(chest.canAccess() || player.isCreative())
				return willHarvest || super.removedByPlayer(state, world, pos, player, false, fluid);
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

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(worldIn.isRemote)
			return true;

		TileEntity tile = worldIn.getTileEntity(pos);
		if(!(tile instanceof TileFrequencyOwner) || !(tile instanceof INamedContainerProvider))
			return false;

		TileFrequencyOwner owner = (TileFrequencyOwner) tile;

		return !player.isSneaking() && owner.activate(player, worldIn, pos, handIn);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TileFrequencyOwner)
		{
			((TileFrequencyOwner) tile).onPlaced(placer);
		}
	}

	@Override
	public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int eventID, int eventParam)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data)
	{
		TileEntity te = world.getTileEntity(data.getPos());
		if(te instanceof TileEntityDimChest)
		{
			TileEntityDimChest tile = (TileEntityDimChest) te;

			if(tile.frequency.hasOwner())
			{
				if(tile.canAccess())
					probeInfo.horizontal().text(TextFormatting.GREEN + "Owner: " + tile.frequency.getOwner());
				else
					probeInfo.horizontal().text(TextFormatting.RED + "Owner: " + tile.frequency.getOwner());
			}
			probeInfo.horizontal().text("Frequency: " + tile.frequency.getChannel());
			if(tile.isLocked())
				probeInfo.horizontal().text("Locked: Yes");
		}
	}
}
