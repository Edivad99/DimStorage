package edivad.dimstorage.compat.top;

import org.apache.logging.log4j.Level;

import com.google.common.base.Function;

import edivad.dimstorage.Main;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.InterModComms;

public class TOPCompatibility {

	private static boolean registered;

	public static void register()
	{
		if(registered)
			return;
		registered = true;

		InterModComms.sendTo("theoneprobe", "getTheOneProbe", () -> (Function<ITheOneProbe, Void>) iTheOneProbe -> {
			Main.logger.log(Level.INFO, "Enabled support for The One Probe");

			iTheOneProbe.registerProvider(new IProbeInfoProvider() {

				@Override
				public String getID()
				{
					return Main.MODID + ":default";
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
			});
			return null;
		});
	}
}
