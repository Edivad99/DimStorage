package edivad.dimstorage.blocks;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

import org.jetbrains.annotations.Nullable;
import edivad.dimstorage.blockentity.BlockEntityDimTank;
import edivad.dimstorage.setup.ModRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DimTankBlock extends DimBlockBase implements SimpleWaterloggedBlock {

  private static final VoxelShape BOX = box(2, 0, 2, 14, 16, 14);

  public DimTankBlock(Properties properties) {
    super(properties.sound(SoundType.GLASS).requiresCorrectToolForDrops().strength(3.5F)
        .noOcclusion());
    this.registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new BlockEntityDimTank(pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
      BlockEntityType<T> blockEntityType) {
    return createDimBlockTicker(level, blockEntityType, ModRegistration.DIMTANK_TILE.get());
  }

  @Override
  protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
      BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
    if (player instanceof ServerPlayer serverPlayer) {
      if (level.getBlockEntity(pos) instanceof BlockEntityDimTank tank) {
        if (!player.isCrouching()) {
          return tank.useItemOn(serverPlayer, level, pos, hand);
        }
      }
    }
    return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
  }

  @Override
  protected VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos,
      CollisionContext context) {
    return BOX;
  }

  @Override
  protected VoxelShape getCollisionShape(BlockState state, BlockGetter blockGetter, BlockPos pos,
      CollisionContext context) {
    return BOX;
  }

  @Override
  public int getLightEmission(BlockState state, BlockGetter blockGetter, BlockPos pos) {
    if (blockGetter.getBlockEntity(pos) instanceof BlockEntityDimTank tank) {
      var fluid = tank.liquidState.clientLiquid;
      if (!fluid.isEmpty()) {
        var fluidType = fluid.getFluid().getFluidType();
        if (blockGetter instanceof BlockAndTintGetter blockAndTint) {
          return fluidType.getLightLevel(fluid.getFluid().defaultFluidState(), blockAndTint, pos);
        } else {
          return fluidType.getLightLevel(fluid);
        }
      }
    }
    return 0;
  }

  @Override
  protected boolean hasAnalogOutputSignal(BlockState state) {
    return true;
  }

  @Override
  protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos,
      Direction direction) {
    return (level.getBlockEntity(pos) instanceof BlockEntityDimTank tank)
        ? tank.getComparatorInput() : 0;
  }

  @Override
  protected FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public boolean placeLiquid(LevelAccessor levelAccessor, BlockPos pos, BlockState state,
      FluidState fluidStateIn) {
    return SimpleWaterloggedBlock.super.placeLiquid(levelAccessor, pos, state, fluidStateIn);
  }

  @Override
  public boolean canPlaceLiquid(@Nullable LivingEntity entity, BlockGetter blockGetter, BlockPos pos,
      BlockState state, Fluid fluidIn) {
    return SimpleWaterloggedBlock.super.canPlaceLiquid(entity, blockGetter, pos, state, fluidIn);
  }

  @Override
  protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(WATERLOGGED);
  }
}
