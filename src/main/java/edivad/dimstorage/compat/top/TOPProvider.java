//package edivad.dimstorage.compat.top;
//
//import edivad.dimstorage.Main;
//import edivad.dimstorage.api.Frequency;
//import edivad.dimstorage.storage.DimTankStorage;
//import edivad.dimstorage.blockentities.BlockEntityDimTank;
//import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
//import mcjty.theoneprobe.api.IElement;
//import mcjty.theoneprobe.api.IElementFactory;
//import mcjty.theoneprobe.api.IProbeHitData;
//import mcjty.theoneprobe.api.IProbeInfo;
//import mcjty.theoneprobe.api.IProbeInfoProvider;
//import mcjty.theoneprobe.api.ITheOneProbe;
//import mcjty.theoneprobe.api.ProbeMode;
//import net.minecraft.ChatFormatting;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.network.chat.TextComponent;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//
//import java.util.function.Function;
//
//public class TOPProvider implements IProbeInfoProvider, Function<ITheOneProbe, Void> {
//
//    @Override
//    public Void apply(ITheOneProbe probe) {
//        probe.registerProvider(this);
//        probe.registerElementFactory(new IElementFactory() {
//
//            @Override
//            public IElement createElement(FriendlyByteBuf friendlyByteBuf) {
//                return new FluidElement(friendlyByteBuf);
//            }
//
//            @Override
//            public ResourceLocation getId() {
//                return FluidElement.ID;
//            }
//        });
//        return null;
//    }
//
//    @Override
//    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData data) {
//        BlockEntity te = level.getBlockEntity(data.getPos());
//
//        if(te instanceof BlockEntityFrequencyOwner owner) {
//            Frequency frequency = owner.getFrequency();
//
//            if(frequency.hasOwner()) {
//                if(owner.canAccess(player))
//                    probeInfo.horizontal().text(new TextComponent(ChatFormatting.GREEN + "Owner: " + frequency.getOwner()));
//                else
//                    probeInfo.horizontal().text(new TextComponent(ChatFormatting.RED + "Owner: " + frequency.getOwner()));
//            }
//            probeInfo.horizontal().text(new TextComponent("Frequency: " + frequency.getChannel()));
//            if(owner.locked)
//                probeInfo.horizontal().text(new TextComponent("Locked: Yes"));
//
//            if(te instanceof BlockEntityDimTank tank) {
//                if(tank.autoEject)
//                    probeInfo.horizontal().text(new TextComponent("Auto-eject: Yes"));
//
//                if(!tank.liquidState.serverLiquid.isEmpty())
//                    probeInfo.element(new FluidElement(tank, DimTankStorage.CAPACITY));
//            }
//        }
//    }
//
//    @Override
//    public ResourceLocation getID() {
//        return new ResourceLocation(Main.MODID,"default");
//    }
//}
