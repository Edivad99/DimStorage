package edivad.dimstorage.items;

import java.util.List;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.tile.TileFrequencyOwner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemDimBase extends BlockItem {

    public ItemDimBase(Block blockIn)
    {
        super(blockIn, Registration.globalProperties);
    }

    private Frequency getFreq(ItemStack stack)
    {
        if(stack.hasTag())
        {
            CompoundTag stackTag = stack.getTagElement("DimStorage");
            if(stackTag != null && stackTag.contains("Frequency"))
            {
                return new Frequency(stackTag.getCompound("Frequency"));
            }
        }
        return new Frequency();
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state)
    {
        if(super.placeBlock(context, state))
        {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            ItemStack stack = context.getItemInHand();

            TileFrequencyOwner tile = (TileFrequencyOwner) world.getBlockEntity(pos);
            tile.setFrequency(getFreq(stack));
            return true;
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        Frequency frequency = getFreq(stack);
        if(frequency.hasOwner())
            tooltip.add(new TranslatableComponent("gui." + Main.MODID + ".owner").append(" " + frequency.getOwner()).withStyle(ChatFormatting.DARK_RED));
        if(stack.hasTag())
            tooltip.add(new TranslatableComponent("gui." + Main.MODID + ".frequency").append(" " + frequency.getChannel()));
    }
}
