package edivad.dimstorage.blocks;

import javax.annotation.Nullable;

import edivad.dimstorage.compat.top.TOPInfoProvider;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tile.TileFrequencyOwner;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class DimChest extends DimBlockBase implements TOPInfoProvider {

	public DimChest()
	{
		super(Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(5.0F));
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
				if(tile.canAccess(player))
					probeInfo.horizontal().text(TextFormatting.GREEN + "Owner: " + tile.frequency.getOwner());
				else
					probeInfo.horizontal().text(TextFormatting.RED + "Owner: " + tile.frequency.getOwner());
			}
			probeInfo.horizontal().text("Frequency: " + tile.frequency.getChannel());
			if(tile.locked)
				probeInfo.horizontal().text("Locked: Yes");
			probeInfo.horizontal().text("Collecting: " + (tile.collect ? "Yes" : "No"));
		}
	}
}
