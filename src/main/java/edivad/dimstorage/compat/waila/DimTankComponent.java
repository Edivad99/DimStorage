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
  private static final MutableComponent LIQUID = Component.translatable(Translations.LIQUID);
  private static final MutableComponent AMOUNT = Component.translatable(Translations.AMOUNT);

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
        tooltip.add(LIQUID.copy().append(" " + liquidName));
        tooltip.add(AMOUNT.copy().append(" " + data.getInt(DimStorage.ID + ".Amount") + " mB"));
      }
    }
  }

  @Override
  public ResourceLocation getUid() {
    return new ResourceLocation(DimStorage.ID, "dim_tank");
  }
}
