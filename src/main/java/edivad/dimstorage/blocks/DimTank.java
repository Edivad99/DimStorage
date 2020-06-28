package edivad.dimstorage.blocks;

import javax.annotation.Nullable;

import edivad.dimstorage.tile.TileEntityDimTank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public class DimTank extends DimBlockBase implements IWaterLoggable {

    private static final VoxelShape BOX = makeCuboidShape(2, 0, 2, 14, 16, 14);
    private static final BooleanProperty WATERLOGGED = BooleanProperty.create("waterlogged");

    public DimTank()
    {
        super(Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(1.0F).notSolid());
        this.setDefaultState(getDefaultState().with(WATERLOGGED, false));
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

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if(worldIn.isRemote)
            return ActionResultType.SUCCESS;

        TileEntity tile = worldIn.getTileEntity(pos);

        if(tile instanceof TileEntityDimTank)
        {
            return ((TileEntityDimTank) tile).activate(player, worldIn, pos, handIn);
        }
        return ActionResultType.FAIL;
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
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileEntityDimTank)
        {
            TileEntityDimTank tank = (TileEntityDimTank) tile;
            FluidStack fluid = tank.liquidState.clientLiquid;
            if(!fluid.isEmpty())
            {
                FluidAttributes attributes = fluid.getFluid().getAttributes();
                return world instanceof IBlockDisplayReader ? attributes.getLuminosity((IBlockDisplayReader) world, pos) : attributes.getLuminosity(fluid);
            }
        }
        return 0;
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        return (te instanceof TileEntityDimTank) ? ((TileEntityDimTank) te).getComparatorInput() : 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state)
    {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn)
    {
        return IWaterLoggable.super.receiveFluid(worldIn, pos, state, fluidStateIn);
    }

    @Override
    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn)
    {
        return IWaterLoggable.super.canContainFluid(worldIn, pos, state, fluidIn);
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(WATERLOGGED);
    }
}
