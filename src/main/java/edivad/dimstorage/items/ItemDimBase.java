package edivad.dimstorage.items;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class ItemDimBase extends BlockItem {

    public ItemDimBase(Block blockIn) {
        super(blockIn, Registration.globalProperties);
    }

    private Frequency getFreq(ItemStack stack) {
        if(stack.hasTag()) {
            CompoundTag stackTag = stack.getTagElement("DimStorage");
            if(stackTag != null && stackTag.contains("Frequency")) {
                return new Frequency(stackTag.getCompound("Frequency"));
            }
        }
        return new Frequency();
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        if(super.placeBlock(context, state)) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            ItemStack stack = context.getItemInHand();

            BlockEntityFrequencyOwner blockentity = (BlockEntityFrequencyOwner) level.getBlockEntity(pos);
            blockentity.setFrequency(getFreq(stack));
            return true;
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagIn) {
        Frequency frequency = getFreq(stack);
        if(frequency.hasOwner())
            tooltip.add(new TranslatableComponent("gui." + Main.MODID + ".owner").append(" " + frequency.getOwner()).withStyle(ChatFormatting.DARK_RED));
        if(stack.hasTag())
            tooltip.add(new TranslatableComponent("gui." + Main.MODID + ".frequency").append(" " + frequency.getChannel()));
    }
}
