package edivad.dimstorage.blocks;

import javax.annotation.Nullable;

import edivad.dimstorage.compat.top.TOPInfoProvider;
import edivad.dimstorage.storage.DimTankStorage;
import edivad.dimstorage.tile.TileEntityDimTank;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DimTank extends DimBlockBase implements TOPInfoProvider {

	private static final VoxelShape BOX = VoxelShapes.create(2 / 16D, 0 / 16D, 2 / 16D, 14 / 16D, 16 / 16D, 14 / 16D);

	public DimTank()
	{
		super(Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(5.0F));
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
		return new TileEntityDimTank();
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;//CUTOUT
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(worldIn.isRemote)
			return true;

		TileEntity tile = worldIn.getTileEntity(pos);
		if(!(tile instanceof TileEntityDimTank) || !(tile instanceof INamedContainerProvider))
			return false;

		TileEntityDimTank owner = (TileEntityDimTank) tile;

		return owner.activate(player, worldIn, pos, handIn);
	}
	
	@Override
	public boolean doesSideBlockRendering(BlockState state, IEnviromentBlockReader world, BlockPos pos, Direction face)
	{
		return face == Direction.UP || face == Direction.DOWN;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return BOX;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return BOX;
	}
	
	@Override
	public int getLightValue(BlockState state, IEnviromentBlockReader world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityDimTank)
		{
			return ((TileEntityDimTank) tile).getLightValue();
		}
		return 0;
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data)
	{
		TileEntity te = world.getTileEntity(data.getPos());
		if(te instanceof TileEntityDimTank)
		{
			TileEntityDimTank tank = (TileEntityDimTank) te;

			if(tank.frequency.hasOwner())
			{
				if(tank.canAccess(player))
					probeInfo.horizontal().text(TextFormatting.GREEN + "Owner: " + tank.frequency.getOwner());
				else
					probeInfo.horizontal().text(TextFormatting.RED + "Owner: " + tank.frequency.getOwner());
			}
			probeInfo.horizontal().text("Frequency: " + tank.frequency.getChannel());
			if(tank.locked)
				probeInfo.horizontal().text("Locked: Yes");
			if(tank.autoEject)
				probeInfo.horizontal().text("Auto-eject: Yes");
			
			if(tank.liquidState.serverLiquid.getAmount() > 0)
			{
				String liquidText = tank.liquidState.serverLiquid.getDisplayName().getFormattedText();
				
				probeInfo.horizontal().text("Liquid: " + liquidText);
				
				//TODO: fix the color
				int liquidColor = tank.liquidState.serverLiquid.getFluid().getAttributes().getColor();
				IProgressStyle color = probeInfo.defaultProgressStyle().filledColor(liquidColor).alternateFilledColor(liquidColor);
				probeInfo.horizontal()
	            .progress(tank.liquidState.serverLiquid.getAmount(), DimTankStorage.CAPACITY, color.suffix(" mB"));
			}
		}
	}
}
