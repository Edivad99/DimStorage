package edivad.dimstorage.compat.top;

import java.util.function.Function;

import edivad.dimstorage.Main;
import edivad.dimstorage.blocks.DimChest;
import edivad.dimstorage.blocks.DimTank;
import edivad.dimstorage.storage.DimTankStorage;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tile.TileEntityDimTank;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
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
		if(blockState.getBlock() instanceof DimChest)
		{
			TileEntity te = world.getTileEntity(data.getPos());
			if(te instanceof TileEntityDimChest)
			{
				TileEntityDimChest tile = (TileEntityDimChest) te;

				if(tile.frequency.hasOwner())
				{
					if(tile.canAccess(player))
						probeInfo.horizontal().text(TextFormatting.GREEN + "Owner: " + tile.frequency.getOwner());
					else
						probeInfo.horizontal().text(TextFormatting.RED + "Owner: " + tile.frequency.getOwner());
				}
				probeInfo.horizontal().text("Frequency: " + tile.frequency.getChannel());
				if(tile.locked)
					probeInfo.horizontal().text("Locked: Yes");
				probeInfo.horizontal().text("Collecting: " + (tile.collect ? "Yes" : "No"));
			}
		}
		else if(blockState.getBlock() instanceof DimTank)
		{
			TileEntity te = world.getTileEntity(data.getPos());
			if(te instanceof TileEntityDimTank)
			{
				TileEntityDimTank tank = (TileEntityDimTank) te;

				if(tank.frequency.hasOwner())
				{
					if(tank.canAccess(player))
						probeInfo.horizontal().text(TextFormatting.GREEN + "Owner: " + tank.frequency.getOwner());
					else
						probeInfo.horizontal().text(TextFormatting.RED + "Owner: " + tank.frequency.getOwner());
				}
				probeInfo.horizontal().text("Frequency: " + tank.frequency.getChannel());
				if(tank.locked)
					probeInfo.horizontal().text("Locked: Yes");
				if(tank.autoEject)
					probeInfo.horizontal().text("Auto-eject: Yes");

				if(!tank.liquidState.serverLiquid.isEmpty())
					probeInfo.element(new FluidElement(tank.liquidState.serverLiquid, DimTankStorage.CAPACITY));
			}
		}
	}

	@Override
	public String getID()
	{
		return Main.MODID + ":default";
	}
}
