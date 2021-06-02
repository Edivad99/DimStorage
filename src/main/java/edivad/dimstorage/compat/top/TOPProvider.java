package edivad.dimstorage.compat.top;

import java.util.function.Function;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.storage.DimTankStorage;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tile.TileEntityDimTank;
import edivad.dimstorage.tile.TileFrequencyOwner;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class TOPProvider implements IProbeInfoProvider, Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe probe)
    {
        probe.registerProvider(this);
        FluidElement.ID = probe.registerElementFactory(FluidElement::new);
        return null;
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data)
    {
        TileEntity te = world.getBlockEntity(data.getPos());

        if(te instanceof TileFrequencyOwner)
        {
            TileFrequencyOwner owner = (TileFrequencyOwner) te;
            Frequency frequency = owner.getFrequency();

            if(frequency.hasOwner())
            {
                if(owner.canAccess(player))
                    probeInfo.horizontal().text(new StringTextComponent(TextFormatting.GREEN + "Owner: " + frequency.getOwner()));
                else
                    probeInfo.horizontal().text(new StringTextComponent(TextFormatting.RED + "Owner: " + frequency.getOwner()));
            }
            probeInfo.horizontal().text(new StringTextComponent("Frequency: " + frequency.getChannel()));
            if(owner.locked)
                probeInfo.horizontal().text(new StringTextComponent("Locked: Yes"));

            if(te instanceof TileEntityDimTank)
            {
                TileEntityDimTank tank = (TileEntityDimTank) te;
                if(tank.autoEject)
                    probeInfo.horizontal().text(new StringTextComponent("Auto-eject: Yes"));

                if(!tank.liquidState.serverLiquid.isEmpty())
                    probeInfo.element(new FluidElement(tank, DimTankStorage.CAPACITY));
            }
        }
    }

    @Override
    public String getID()
    {
        return Main.MODID + ":default";
    }
}
