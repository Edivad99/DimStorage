package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
import edivad.dimstorage.blockentities.BlockEntityDimChest;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.blocks.DimChest;
import edivad.dimstorage.blocks.DimTank;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(Main.MODID)
public class WailaCompatibility implements IWailaPlugin {

    private static final DimBlockBaseProvider DIM_BLOCK_BASE_PROVIDER = new DimBlockBaseProvider();
    private static final DimTankProvider DIM_TANK_PROVIDER = new DimTankProvider();

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(DIM_BLOCK_BASE_PROVIDER, BlockEntityDimChest.class);
        registration.registerBlockDataProvider(DIM_TANK_PROVIDER, BlockEntityDimTank.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(DIM_BLOCK_BASE_PROVIDER, DimChest.class);
        registration.registerBlockComponent(DIM_TANK_PROVIDER, DimTank.class);
    }
}
