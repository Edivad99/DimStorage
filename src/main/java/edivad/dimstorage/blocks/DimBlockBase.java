package edivad.dimstorage.blocks;

import edivad.dimstorage.tile.TileFrequencyOwner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DimBlockBase extends Block {

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
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid)
    {
        TileEntity tile = world.getBlockEntity(pos);
        if(tile instanceof TileFrequencyOwner)
        {
            TileFrequencyOwner block = (TileFrequencyOwner) tile;
            if(block.canAccess(player) || player.isCreative())
                return willHarvest || super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
        }
        return false;
    }

    @Override
    public void playerDestroy(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack)
    {
        super.playerDestroy(worldIn, player, pos, state, te, stack);
        worldIn.removeBlockEntity(pos);
        worldIn.removeBlock(pos, false);
    }
}
