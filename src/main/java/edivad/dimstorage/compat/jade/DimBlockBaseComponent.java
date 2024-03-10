package edivad.dimstorage.compat.jade;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import edivad.dimstorage.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class DimBlockBaseComponent implements IBlockComponentProvider {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    if (accessor.getBlockEntity() instanceof BlockEntityFrequencyOwner) {
      var tag = accessor.getServerData();
      var has_owner = tag.getBoolean("has_owner");
      var can_access = tag.getBoolean("can_access");
      var owner = tag.getString("owner");
      var frequency = tag.getInt("frequency");
      var locked = tag.getBoolean("locked");

      if (has_owner) {
        var textColor = can_access ? ChatFormatting.GREEN : ChatFormatting.RED;
        tooltip.add(Component.translatable(Translations.OWNER).append(" " + owner).withStyle(textColor));
      }
      tooltip.add(Component.translatable(Translations.FREQUENCY).append(" " + frequency));
      if (locked) {
        tooltip.add(Component.translatable(Translations.LOCKED).append(" ")
            .append(Component.translatable(Translations.YES)));
      }
    }
  }

  @Override
  public ResourceLocation getUid() {
    return DimStorage.rl("dim_block_base");
  }
}
