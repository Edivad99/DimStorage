package edivad.dimstorage.items;

import java.util.List;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import edivad.dimstorage.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemDimBase extends BlockItem {

  public ItemDimBase(Block blockIn) {
    super(blockIn, new Properties());
  }

  private Frequency getFreq(ItemStack stack) {
    if (stack.hasTag()) {
      CompoundTag stackTag = stack.getTagElement("DimStorage");
      if (stackTag != null && stackTag.contains("Frequency")) {
        return new Frequency(stackTag.getCompound("Frequency"));
      }
    }
    return new Frequency();
  }

  @Override
  protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
    if (super.placeBlock(context, state)) {
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
  public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip,
      TooltipFlag flagIn) {
    Frequency frequency = getFreq(stack);
    if (frequency.hasOwner()) {
      tooltip.add(Component.translatable(Translations.OWNER).append(" " + frequency.getOwner())
          .withStyle(ChatFormatting.DARK_RED));
    }
    if (stack.hasTag()) {
      tooltip.add(
          Component.translatable(Translations.FREQUENCY).append(" " + frequency.getChannel()));
    }
  }
}
