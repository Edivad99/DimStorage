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

public class DimTankComponentProvider extends DimBlockBaseComponentProvider implements IComponentProvider {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
    {
        super.appendBody(tooltip, accessor, config);
        if(accessor.getTileEntity() instanceof TileEntityDimTank)
        {
            CompoundNBT data = accessor.getServerData();

            String autoEject = Translate.translateToLocal("gui." + Main.MODID + ".eject") + ": ";
            String yes = Translate.translateToLocal("gui." + Main.MODID + ".yes");
            String liquid = Translate.translateToLocal("gui." + Main.MODID + ".liquid");
            String amount = Translate.translateToLocal("gui." + Main.MODID + ".amount");

            if(data.getBoolean("AutoEject"))
                tooltip.add(new StringTextComponent(autoEject + yes));
            if(data.getInt("Amount") > 0)
            {
                tooltip.add(new StringTextComponent(liquid + " " + data.getString("Liquid")));
                tooltip.add(new StringTextComponent(amount + " " + data.getInt("Amount") + " mB"));
            }

        }
    }
}