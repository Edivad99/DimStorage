package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class DimBlockBaseComponentProvider implements IComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        //Remove extra info from Waila
        tooltip.clear();
        if(accessor.getBlockEntity() instanceof BlockEntityFrequencyOwner) {
            CompoundTag data = accessor.getServerData();

            String owner = new TranslatableComponent("gui." + Main.MODID + ".owner").getString() + " ";
            String freq = new TranslatableComponent("gui." + Main.MODID + ".frequency").getString() + " ";
            String locked = new TranslatableComponent("gui." + Main.MODID + ".locked").getString() + " ";
            String yes = new TranslatableComponent("gui." + Main.MODID + ".yes").getString();

            if(data.getBoolean(Main.MODID + ".HasOwner")) {
                if(data.getBoolean(Main.MODID + ".CanAccess"))
                    tooltip.add(new TextComponent(ChatFormatting.GREEN + owner + data.getString(Main.MODID + ".Owner")));
                else
                    tooltip.add(new TextComponent(ChatFormatting.RED + owner + data.getString(Main.MODID + ".Owner")));
            }
            tooltip.add(new TextComponent(freq + data.getInt(Main.MODID + ".Frequency")));
            if(data.getBoolean(Main.MODID + ".Locked"))
                tooltip.add(new TextComponent(locked + yes));
        }
    }
}