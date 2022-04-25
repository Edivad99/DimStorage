package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
import edivad.dimstorage.blocks.DimChest;
import edivad.dimstorage.blocks.DimTank;
import edivad.dimstorage.blockentities.BlockEntityDimChest;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaCommonRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin(Main.MODID)
public class WailaCompatibility implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(DimBlockBaseProvider.INSTANCE, BlockEntityDimChest.class);
        registration.registerBlockDataProvider(new DimTankProvider(), BlockEntityDimTank.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerComponentProvider(DimBlockBaseProvider.INSTANCE, TooltipPosition.BODY, DimChest.class);
        registration.registerComponentProvider(DimTankProvider.INSTANCE, TooltipPosition.BODY, DimTank.class);
    }
}
