package edivad.dimstorage.compat.top;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
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
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

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
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, BlockState state, IProbeHitData data) {
        BlockEntity te = level.getBlockEntity(data.getPos());

        if(te instanceof BlockEntityFrequencyOwner frequencyOwner) {
            MutableComponent locked = Component.translatable(Translations.LOCKED);
            MutableComponent yes = Component.translatable(Translations.YES);
            MutableComponent eject = Component.translatable(Translations.EJECT);
            MutableComponent owner = Component.translatable(Translations.OWNER);
            MutableComponent frequency = Component.translatable(Translations.FREQUENCY);

            Frequency blockFrequency = frequencyOwner.getFrequency();
            if(blockFrequency.hasOwner()) {
                ChatFormatting textColor = frequencyOwner.canAccess(player) ? ChatFormatting.GREEN : ChatFormatting.RED;
                probeInfo.horizontal().text(owner.append(" " + blockFrequency.getOwner()).withStyle(textColor));
            }
            probeInfo.horizontal().text(frequency.append(" " + blockFrequency.getChannel()));
            if(frequencyOwner.locked)
                probeInfo.horizontal().text(locked.append(" ").append(yes));

            if(te instanceof BlockEntityDimTank tank) {
                if(tank.autoEject)
                    probeInfo.horizontal().text(eject.append(" ").append(yes));

                if(!tank.liquidState.serverLiquid.isEmpty())
                    probeInfo.element(new MyFluidElement(tank, DimTankStorage.CAPACITY));
            }
        }
    }

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(Main.MODID,"default");
    }
}
