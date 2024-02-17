package edivad.dimstorage.compat.top;

import java.util.function.Function;
import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import edivad.dimstorage.storage.DimTankStorage;
import edivad.dimstorage.tools.Translations;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IElementFactory;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TOPProvider implements IProbeInfoProvider, Function<ITheOneProbe, Void> {

  @Override
  public Void apply(ITheOneProbe probe) {
    probe.registerProvider(this);
    probe.registerElementFactory(new IElementFactory() {

      @Override
      public IElement createElement(FriendlyByteBuf friendlyByteBuf) {
        return new MyFluidElement(friendlyByteBuf);
      }

      @Override
      public ResourceLocation getId() {
        return MyFluidElement.ID;
      }
    });
    return null;
  }

  @Override
  public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level,
      BlockState state, IProbeHitData data) {

    if (level.getBlockEntity(data.getPos()) instanceof BlockEntityFrequencyOwner frequencyOwner) {
      var locked = Component.translatable(Translations.LOCKED);
      var yes = Component.translatable(Translations.YES);
      var eject = Component.translatable(Translations.EJECT);
      var owner = Component.translatable(Translations.OWNER);
      var frequency = Component.translatable(Translations.FREQUENCY);

      var blockFrequency = frequencyOwner.getFrequency();
      if (blockFrequency.hasOwner()) {
        var textColor =
            frequencyOwner.canAccess(player) ? ChatFormatting.GREEN : ChatFormatting.RED;
        probeInfo.horizontal()
            .text(owner.append(" " + blockFrequency.getOwner()).withStyle(textColor));
      }
      probeInfo.horizontal().text(frequency.append(" " + blockFrequency.getChannel()));
      if (frequencyOwner.locked) {
        probeInfo.horizontal().text(locked.append(" ").append(yes));
      }

      if (level.getBlockEntity(data.getPos()) instanceof BlockEntityDimTank tank) {
        if (tank.autoEject) {
          probeInfo.horizontal().text(eject.append(" ").append(yes));
        }

        if (!tank.liquidState.serverLiquid.isEmpty()) {
          probeInfo.element(new MyFluidElement(tank, DimTankStorage.CAPACITY));
        }
      }
    }
  }

  @Override
  public ResourceLocation getID() {
    return DimStorage.rl("default");
  }
}
