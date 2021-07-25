package edivad.dimstorage.blocks;

import javax.annotation.Nullable;

import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.tile.TileEntityDimTank;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public class DimTank extends DimBlockBase implements SimpleWaterloggedBlock {

    private static final VoxelShape BOX = box(2, 0, 2, 14, 16, 14);
    private static final BooleanProperty WATERLOGGED = BooleanProperty.create("waterlogged");

    public DimTank()
    {
        super(Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(1.0F).noOcclusion());
        this.registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return new TileEntityDimTank(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType)
    {
        return createDimBlockTicker(level, blockEntityType, Registration.DIMTANK_TILE.get());
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
    {
        if(worldIn.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity tile = worldIn.getBlockEntity(pos);

        if(tile instanceof TileEntityDimTank)
        {
            if(!player.isCrouching())
                return ((TileEntityDimTank) tile).activate(player, worldIn, pos, handIn);
        }
        return InteractionResult.FAIL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return BOX;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return BOX;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos)
    {
        BlockEntity tile = world.getBlockEntity(pos);
        if(tile instanceof TileEntityDimTank)
        {
            TileEntityDimTank tank = (TileEntityDimTank) tile;
            FluidStack fluid = tank.liquidState.clientLiquid;
            if(!fluid.isEmpty())
            {
                FluidAttributes attributes = fluid.getFluid().getAttributes();
                return world instanceof BlockAndTintGetter ? attributes.getLuminosity((BlockAndTintGetter) world, pos) : attributes.getLuminosity(fluid);
            }
        }
        return 0;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos)
    {
        BlockEntity te = worldIn.getBlockEntity(pos);
        return (te instanceof TileEntityDimTank) ? ((TileEntityDimTank) te).getComparatorInput() : 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state)
    {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(LevelAccessor worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn)
    {
        return SimpleWaterloggedBlock.super.placeLiquid(worldIn, pos, state, fluidStateIn);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter worldIn, BlockPos pos, BlockState state, Fluid fluidIn)
    {
        return SimpleWaterloggedBlock.super.canPlaceLiquid(worldIn, pos, state, fluidIn);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }
}
