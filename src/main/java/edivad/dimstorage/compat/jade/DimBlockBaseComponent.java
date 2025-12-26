package edivad.dimstorage.compat.jade;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blockentity.BlockEntityFrequencyOwner;
import edivad.dimstorage.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class DimBlockBaseComponent implements IBlockComponentProvider {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    if (accessor.getBlockEntity() instanceof BlockEntityFrequencyOwner blockEntity) {
      var tag = accessor.getServerData();
      var frequency = tag.read("frequency", Frequency.CODEC).orElseThrow();
      var locked = tag.getBoolean("locked").orElse(false);

      if (frequency.hasOwner()) {
        var textColor = blockEntity.canAccess(accessor.getPlayer())
            ? ChatFormatting.GREEN : ChatFormatting.RED;
        tooltip.add(Component.translatable(Translations.OWNER).append(" " + frequency.getOwner()).withStyle(textColor));
      }
      tooltip.add(Component.translatable(Translations.FREQUENCY).append(" " + frequency.channel()));

      if (locked) {
        tooltip.add(Component.translatable(Translations.LOCKED).append(" ")
            .append(Component.translatable(Translations.YES)));
      }
    }
  }

  @Override
  public Identifier getUid() {
    return DimStorage.id("dim_block_base");
  }
}
