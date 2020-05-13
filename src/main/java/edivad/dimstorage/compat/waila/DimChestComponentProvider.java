package edivad.dimstorage.compat.waila;

import java.util.List;

import edivad.dimstorage.Main;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tools.Translate;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class DimChestComponentProvider extends DimBlockBaseComponentProvider implements IComponentProvider {

	@Override
	public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
	{
		super.appendBody(tooltip, accessor, config);
		if(accessor.getTileEntity() instanceof TileEntityDimChest)
		{
			CompoundNBT data = accessor.getServerData();

			String yes = Translate.translateToLocal("gui." + Main.MODID + ".yes");
			String no = Translate.translateToLocal("gui." + Main.MODID + ".no");
			String collecting = Translate.translateToLocal("gui." + Main.MODID + ".collecting") + ": ";

			tooltip.add(new StringTextComponent(collecting + (data.getBoolean("Collecting") ? yes : no)));
		}
	}
}