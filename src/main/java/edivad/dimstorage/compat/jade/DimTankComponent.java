package edivad.dimstorage.compat.jade;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentity.BlockEntityDimTank;
import edivad.dimstorage.tools.Translations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class DimTankComponent extends DimBlockBaseComponent implements IBlockComponentProvider {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    super.appendTooltip(tooltip, accessor, config);
    if (accessor.getBlockEntity() instanceof BlockEntityDimTank) {
      var tag = accessor.getServerData();
      var auto_eject = tag.getBoolean("auto_eject").orElse(false);

      if (auto_eject) {
        tooltip.add(Component.translatable(Translations.EJECT, Component.translatable(Translations.YES)));
      }
    }
  }

  @Override
  public Identifier getUid() {
    return DimStorage.id("dim_tank");
  }
}
