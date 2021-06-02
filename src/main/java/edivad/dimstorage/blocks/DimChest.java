package edivad.dimstorage.blocks;

import javax.annotation.Nullable;

import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class DimChest extends DimBlockBase {

    public DimChest()
    {
        super(Properties.of(Material.STONE).sound(SoundType.STONE).strength(5.0F).noOcclusion());
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
    public BlockRenderType getRenderShape(BlockState state)
    {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if(worldIn.isClientSide)
            return ActionResultType.SUCCESS;

        TileEntity tile = worldIn.getBlockEntity(pos);

        if(tile instanceof TileEntityDimChest)
        {
            if(!player.isCrouching())
                return ((TileEntityDimChest) tile).activate(player, worldIn, pos, handIn);
        }
        return ActionResultType.FAIL;
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        TileEntity tile = worldIn.getBlockEntity(pos);
        if(tile instanceof TileEntityDimChest)
        {
            ((TileEntityDimChest) tile).onPlaced(placer);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos)
    {
        TileEntity te = worldIn.getBlockEntity(pos);
        return (te instanceof TileEntityDimChest) ? ((TileEntityDimChest) te).getComparatorInput() : 0;
    }

    @Override
    public boolean triggerEvent(BlockState state, World worldIn, BlockPos pos, int eventID, int eventParam)
    {
        TileEntity tileentity = worldIn.getBlockEntity(pos);
        return tileentity != null && tileentity.triggerEvent(eventID, eventParam);
    }
}
