package edivad.dimstorage.compat.waila;

import java.util.List;

import edivad.dimstorage.tile.TileEntityDimChest;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class DimChestComponentProvider implements IComponentProvider {

	@Override
	public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
	{
		if(accessor.getTileEntity() instanceof TileEntityDimChest)
		{
			CompoundNBT data = accessor.getServerData();
			if(data.getBoolean("HasOwner"))
			{
				if(data.getBoolean("CanAccess"))
					tooltip.add(new StringTextComponent(TextFormatting.GREEN + "Owner: " + data.getString("Owner")));
				else
					tooltip.add(new StringTextComponent(TextFormatting.RED + "Owner: " + data.getString("Owner")));
			}
			tooltip.add(new StringTextComponent("Frequency: " + data.getInt("Frequency")));
			if(data.getBoolean("Locked"))
				tooltip.add(new StringTextComponent("Locked: Yes"));
		}
	}
}