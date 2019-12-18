package edivad.dimstorage.compat.top;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import com.google.common.base.Function;

import edivad.dimstorage.Main;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class TOPCompatibility {

	private static boolean registered;

	public static void register()
	{
		if(registered)
			return;
		registered = true;
		FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "edivad.dimstorage.compat.top.TOPCompatibility$GetTheOneProbe");
	}

	public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {

		public static ITheOneProbe probe;

		@Nullable
		@Override
		public Void apply(ITheOneProbe theOneProbe)
		{
			probe = theOneProbe;
			Main.logger.log(Level.INFO, "Enabled support for The One Probe");
			probe.registerProvider(new IProbeInfoProvider() {

				@Override
				public String getID()
				{
					return Main.MODID + ":default";
				}

				@Override
				public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data)
				{
					if(blockState.getBlock() instanceof TOPInfoProvider)
					{
						TOPInfoProvider provider = (TOPInfoProvider) blockState.getBlock();
						provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
					}
				}
			});
			return null;
		}
	}
}
