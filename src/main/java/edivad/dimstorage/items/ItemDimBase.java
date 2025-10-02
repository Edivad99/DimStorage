package edivad.dimstorage.items;

import java.util.function.Consumer;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blockentity.BlockEntityFrequencyOwner;
import edivad.dimstorage.items.components.DimStorageComponents;
import edivad.dimstorage.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ItemDimBase extends BlockItem {

  public ItemDimBase(Block block, Properties properties) {
    super(block, properties);
  }

  private Frequency getFreq(ItemStack stack) {
    return stack.getOrDefault(DimStorageComponents.FREQUENCY, new Frequency());
  }

  @Override
  protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
    if (super.placeBlock(context, state)) {
      var level = context.getLevel();
      var pos = context.getClickedPos();
      var stack = context.getItemInHand();
      if (level.getBlockEntity(pos) instanceof BlockEntityFrequencyOwner b) {
        b.setFrequency(getFreq(stack));
        return true;
      }
    }
    return false;
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    var frequency = getFreq(stack);
    if (frequency.hasOwner()) {
      tooltipAdder.accept(Component.translatable(Translations.OWNER).append(" " + frequency.getOwner())
          .withStyle(ChatFormatting.DARK_RED));
    }
    if (stack.has(DimStorageComponents.FREQUENCY)) {
      tooltipAdder.accept(Component.translatable(Translations.FREQUENCY)
          .append(" " + frequency.channel()));
    }
  }
}
