package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
import edivad.dimstorage.blocks.DimChest;
import edivad.dimstorage.blocks.DimTank;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tile.TileEntityDimTank;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin(Main.MODID)
public class WailaCompatibility implements IWailaPlugin {

    @Override
    public void register(IRegistrar iRegistrar)
    {
        iRegistrar.registerBlockDataProvider(new DimBlockBaseProvider(), TileEntityDimChest.class);
        iRegistrar.registerComponentProvider(new DimBlockBaseComponentProvider(), TooltipPosition.BODY, DimChest.class);

        iRegistrar.registerBlockDataProvider(new DimTankDataProvider(), TileEntityDimTank.class);
        iRegistrar.registerComponentProvider(new DimTankComponentProvider(), TooltipPosition.BODY, DimTank.class);
    }
}
