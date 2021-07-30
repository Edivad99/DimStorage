package edivad.dimstorage.blocks;

import edivad.dimstorage.tile.TileFrequencyOwner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public abstract class DimBlockBase extends Block implements EntityBlock {

    public DimBlockBase(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean hasDynamicShape()
    {
        return false;
    }

    @Override
    public boolean removedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid)
    {
        BlockEntity tile = world.getBlockEntity(pos);
        if(tile instanceof TileFrequencyOwner)
        {
            TileFrequencyOwner block = (TileFrequencyOwner) tile;
            if(block.canAccess(player) || player.isCreative())
                return willHarvest || super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
        }
        return false;
    }

    @Override
    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, BlockEntity te, ItemStack stack)
    {
        super.playerDestroy(worldIn, player, pos, state, te, stack);
        worldIn.removeBlockEntity(pos);
        worldIn.removeBlock(pos, false);
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createDimBlockTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends TileFrequencyOwner> tile) {
        BlockEntityTicker<TileFrequencyOwner> serverTicker = TileFrequencyOwner::serverTick;
        BlockEntityTicker<TileFrequencyOwner> clientTicker = TileFrequencyOwner::clientTick;
        if(tile == blockEntityType) {
            return level.isClientSide ? (BlockEntityTicker<T>) clientTicker : (BlockEntityTicker<T>) serverTicker;
        }
        return null;
    }
}
