package edivad.dimstorage.compat.waila;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.tools.Translations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class DimTankComponent extends DimBlockBaseComponent implements IBlockComponentProvider {

  private static final MutableComponent AUTO_EJECT = Component.translatable(Translations.EJECT);
  private static final MutableComponent YES = Component.translatable(Translations.YES);

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    super.appendTooltip(tooltip, accessor, config);
    if (accessor.getBlockEntity() instanceof BlockEntityDimTank) {
      CompoundTag data = accessor.getServerData();

      if (data.getBoolean(DimStorage.ID + ".AutoEject")) {
        tooltip.add(AUTO_EJECT.copy().append(": ").append(YES));
      }
      if (data.getInt(DimStorage.ID + ".Amount") > 0) {
        String liquidName = Component.translatable(data.getString(DimStorage.ID + ".Liquid"))
            .getString();
        tooltip.add(Component.translatable(Translations.LIQUID, liquidName));
        tooltip.add(Component.translatable(Translations.AMOUNT, data.getInt(DimStorage.ID + ".Amount")));
      }
    }
  }

  @Override
  public ResourceLocation getUid() {
    return DimStorage.rl("dim_tank");
  }
}
