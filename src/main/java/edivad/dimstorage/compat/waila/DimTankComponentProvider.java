package edivad.dimstorage.compat.waila;

import java.util.List;

import edivad.dimstorage.Main;
import edivad.dimstorage.tile.TileEntityDimTank;
import edivad.dimstorage.tools.Translate;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class DimTankComponentProvider implements IComponentProvider {

	@Override
	public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
	{
		if(accessor.getTileEntity() instanceof TileEntityDimTank)
		{
			CompoundNBT data = accessor.getServerData();

			String owner = Translate.translateToLocal("gui." + Main.MODID + ".owner") + " ";
			String freq = Translate.translateToLocal("gui." + Main.MODID + ".frequency") + " ";
			String locked = Translate.translateToLocal("gui." + Main.MODID + ".locked") + " ";
			String yes = Translate.translateToLocal("gui." + Main.MODID + ".yes");
			
			if(data.getBoolean("HasOwner"))
			{
				if(data.getBoolean("CanAccess"))
					tooltip.add(new StringTextComponent(TextFormatting.GREEN + owner + data.getString("Owner")));
				else
					tooltip.add(new StringTextComponent(TextFormatting.RED + owner + data.getString("Owner")));
			}
			tooltip.add(new StringTextComponent(freq + data.getInt("Frequency")));
			if(data.getBoolean("Locked"))
				tooltip.add(new StringTextComponent(locked + yes));
			if(data.getInt("Amount") > 0)
			{
				tooltip.add(new StringTextComponent("Liquid: " + data.getString("Liquid")));
				tooltip.add(new StringTextComponent("Amount: " + data.getInt("Amount") + " mB"));
			}
			
		}
	}
}