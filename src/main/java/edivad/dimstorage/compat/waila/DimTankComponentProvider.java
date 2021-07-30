package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
import edivad.dimstorage.tile.TileEntityDimTank;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class DimTankComponentProvider extends DimBlockBaseComponentProvider implements IComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config)
    {
        super.appendTooltip(tooltip, accessor, config);
        if(accessor.getBlockEntity() instanceof TileEntityDimTank)
        {
            CompoundTag data = accessor.getServerData();

            String autoEject = new TranslatableComponent("gui." + Main.MODID + ".eject").getString() + ": ";
            String yes = new TranslatableComponent("gui." + Main.MODID + ".yes").getString();
            String liquid = new TranslatableComponent("gui." + Main.MODID + ".liquid").getString();
            String amount = new TranslatableComponent("gui." + Main.MODID + ".amount").getString();

            if(data.getBoolean(Main.MODID + ".AutoEject"))
                tooltip.add(new TextComponent(autoEject + yes));
            if(data.getInt(Main.MODID + ".Amount") > 0)
            {
                tooltip.add(new TextComponent(liquid + " " + data.getString(Main.MODID + ".Liquid")));
                tooltip.add(new TextComponent(amount + " " + data.getInt(Main.MODID + ".Amount") + " mB"));
            }

        }
    }
}