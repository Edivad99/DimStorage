package edivad.dimstorage.blocks;

import javax.annotation.Nullable;

import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;

public class DimChest extends DimBlockBase {

    public DimChest()
    {
        super(Properties.of(Material.STONE).sound(SoundType.STONE).strength(5.0F).noOcclusion());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return new TileEntityDimChest(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType)
    {
        return createDimBlockTicker(level, blockEntityType, Registration.DIMCHEST_TILE.get());
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
    {
        if(worldIn.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity tile = worldIn.getBlockEntity(pos);

        if(tile instanceof TileEntityDimChest)
        {
            if(!player.isCrouching())
                return ((TileEntityDimChest) tile).activate(player, worldIn, pos, handIn);
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        BlockEntity tile = worldIn.getBlockEntity(pos);
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
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos)
    {
        BlockEntity te = worldIn.getBlockEntity(pos);
        return (te instanceof TileEntityDimChest) ? ((TileEntityDimChest) te).getComparatorInput() : 0;
    }

    @Override
    public boolean triggerEvent(BlockState state, Level worldIn, BlockPos pos, int eventID, int eventParam)
    {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        return tileentity != null && tileentity.triggerEvent(eventID, eventParam);
    }
}
