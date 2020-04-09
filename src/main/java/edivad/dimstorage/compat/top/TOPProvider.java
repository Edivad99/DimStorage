package edivad.dimstorage.compat.top;

import java.util.function.Function;

import edivad.dimstorage.Main;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
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
	public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData iProbeHitData)
	{
		if(blockState.getBlock() instanceof TOPInfoProvider)
		{
			TOPInfoProvider provider = (TOPInfoProvider) blockState.getBlock();
			provider.addProbeInfo(probeMode, probeInfo, player, world, blockState, iProbeHitData);
		}
	}

	@Override
	public String getID()
	{
		return Main.MODID + ":default";
	}
}
