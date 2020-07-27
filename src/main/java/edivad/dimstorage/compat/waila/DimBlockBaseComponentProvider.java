package edivad.dimstorage.compat.waila;

import java.util.List;

import edivad.dimstorage.Main;
import edivad.dimstorage.tile.TileFrequencyOwner;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class DimBlockBaseComponentProvider implements IComponentProvider {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
    {
        if(accessor.getTileEntity() instanceof TileFrequencyOwner)
        {
            CompoundNBT data = accessor.getServerData();

            String owner = new TranslationTextComponent("gui." + Main.MODID + ".owner").getString() + " ";
            String freq = new TranslationTextComponent("gui." + Main.MODID + ".frequency").getString() + " ";
            String locked = new TranslationTextComponent("gui." + Main.MODID + ".locked").getString() + " ";
            String yes = new TranslationTextComponent("gui." + Main.MODID + ".yes").getString();

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
        }
    }
}