//package edivad.dimstorage.compat.waila;
//
//import edivad.dimstorage.Main;
//import edivad.dimstorage.blocks.DimChest;
//import edivad.dimstorage.blocks.DimTank;
//import edivad.dimstorage.blockentities.BlockEntityDimChest;
//import edivad.dimstorage.blockentities.BlockEntityDimTank;
//import mcp.mobius.waila.api.IRegistrar;
//import mcp.mobius.waila.api.IWailaPlugin;
//import mcp.mobius.waila.api.TooltipPosition;
//import mcp.mobius.waila.api.WailaPlugin;
//
//@WailaPlugin(Main.MODID)
//public class WailaCompatibility implements IWailaPlugin {
//
//    @Override
//    public void register(IRegistrar iRegistrar) {
//        iRegistrar.registerBlockDataProvider(new DimBlockBaseProvider(), BlockEntityDimChest.class);
//        iRegistrar.registerComponentProvider(new DimBlockBaseComponentProvider(), TooltipPosition.BODY, DimChest.class);
//
//        iRegistrar.registerBlockDataProvider(new DimTankDataProvider(), BlockEntityDimTank.class);
//        iRegistrar.registerComponentProvider(new DimTankComponentProvider(), TooltipPosition.BODY, DimTank.class);
//    }
//}
