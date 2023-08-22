package edivad.dimstorage.blocks;

import org.jetbrains.annotations.Nullable;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public abstract class DimBlockBase extends Block implements EntityBlock {

  public DimBlockBase(Properties properties) {
    super(properties);
  }

  @Nullable
  protected static <T extends BlockEntity> BlockEntityTicker<T> createDimBlockTicker(Level level,
      BlockEntityType<T> blockEntityType,
      BlockEntityType<? extends BlockEntityFrequencyOwner> blockentity) {
    return level.isClientSide()
        ? BaseEntityBlock.createTickerHelper(blockEntityType, blockentity,
        BlockEntityFrequencyOwner::clientTick)
        : BaseEntityBlock.createTickerHelper(blockEntityType, blockentity,
            BlockEntityFrequencyOwner::serverTick);
  }

  @Override
  public boolean hasDynamicShape() {
    return false;
  }

  @Override
  public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
      boolean willHarvest, FluidState fluid) {
    BlockEntity blockentity = level.getBlockEntity(pos);
    if (blockentity instanceof BlockEntityFrequencyOwner block) {
      if (block.canAccess(player) || player.isCreative()) {
        return willHarvest || super.onDestroyedByPlayer(state, level, pos, player, false, fluid);
      }
    }
    return false;
  }

  @Override
  public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
      BlockEntity te, ItemStack stack) {
    super.playerDestroy(level, player, pos, state, te, stack);
    level.removeBlockEntity(pos);
    level.removeBlock(pos, false);
  }
}
