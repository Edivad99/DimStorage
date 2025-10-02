package edivad.dimstorage.items.components;

import java.util.function.Consumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record FrequencyTabletComponent(Frequency frequency, boolean bound, boolean autocollect) implements
    TooltipProvider {

  public static final Codec<FrequencyTabletComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          Frequency.CODEC.fieldOf("frequency").forGetter(FrequencyTabletComponent::frequency),
          Codec.BOOL.fieldOf("bound").forGetter(FrequencyTabletComponent::bound),
          Codec.BOOL.fieldOf("autocollect").forGetter(FrequencyTabletComponent::autocollect)
      ).apply(instance, FrequencyTabletComponent::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, FrequencyTabletComponent> STREAM_CODEC =
      StreamCodec.composite(
          Frequency.STREAM_CODEC, FrequencyTabletComponent::frequency,
          ByteBufCodecs.BOOL, FrequencyTabletComponent::bound,
          ByteBufCodecs.BOOL, FrequencyTabletComponent::autocollect,
          FrequencyTabletComponent::new);

  @Override
  public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder,
      TooltipFlag flag, DataComponentGetter componentGetter) {
    var HOLD_SHIFT = Component.translatable(Translations.HOLD)
        .withStyle(ChatFormatting.GRAY)
        .append(" ")
        .append(Component.literal("Shift").withStyle(ChatFormatting.ITALIC, ChatFormatting.AQUA))
        .append(" ")
        .append(Component.translatable(Translations.FOR_DETAILS).withStyle(ChatFormatting.GRAY));

    var CHANGE_AUTOCOLLECT = Component.translatable(Translations.PRESS)
        .withStyle(ChatFormatting.GRAY)
        .append(" ")
        .append(Component.literal("Shift").withStyle(ChatFormatting.ITALIC, ChatFormatting.AQUA))
        .append(Component.literal(" + ").withStyle(ChatFormatting.GRAY))
        .append(Component.translatable(Translations.CHANGE_AUTO_COLLECT)
            .withStyle(ChatFormatting.GRAY));

    if (Minecraft.getInstance().hasShiftDown()) {
      tooltipAdder.accept(Component.translatable(Translations.FREQUENCY).append(" " + frequency.channel())
          .withStyle(ChatFormatting.GRAY));
      if (frequency.hasOwner()) {
        tooltipAdder.accept(Component.translatable(Translations.OWNER).append(" " + frequency.getOwner())
            .withStyle(ChatFormatting.GRAY));
      }

      var yes = Component.translatable(Translations.YES);
      var no = Component.translatable(Translations.NO);
      var collecting = Component.translatable(Translations.COLLECTING);
      tooltipAdder.accept(collecting.append(": ")
          .append(autocollect ? yes : no)
          .withStyle(ChatFormatting.GRAY));
    } else {
      tooltipAdder.accept(HOLD_SHIFT);
    }

    tooltipAdder.accept(CHANGE_AUTOCOLLECT);
  }
}
