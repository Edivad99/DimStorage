package edivad.dimstorage.compat.jade;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentities.BlockEntityDimChest;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.blocks.DimChest;
import edivad.dimstorage.blocks.DimTank;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(DimStorage.ID)
public class JadeCompatibility implements IWailaPlugin {

  @Override
  public void register(IWailaCommonRegistration registration) {
    registration.registerBlockDataProvider(new DimBlockBaseProvider(), BlockEntityDimChest.class);
    registration.registerBlockDataProvider(new DimTankProvider(), BlockEntityDimTank.class);
  }

  @Override
  public void registerClient(IWailaClientRegistration registration) {
    registration.registerBlockComponent(new DimBlockBaseComponent(), DimChest.class);
    registration.registerBlockComponent(new DimTankComponent(), DimTank.class);
  }
}
