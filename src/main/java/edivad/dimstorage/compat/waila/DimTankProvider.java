package edivad.dimstorage.compat.waila;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;

public class DimTankProvider extends DimBlockBaseProvider {

  @Override
  public void appendServerData(CompoundTag compoundTag, BlockAccessor accessor) {
    BlockEntityDimTank blockEntity = (BlockEntityDimTank) accessor.getBlockEntity();
    compoundTag.putBoolean(DimStorage.ID + ".AutoEject", blockEntity.autoEject);
    String liquidName = blockEntity.liquidState.serverLiquid.getFluid().getFluidType()
        .getDescriptionId();
    int liquidLevel = blockEntity.liquidState.serverLiquid.getAmount();
    compoundTag.putString(DimStorage.ID + ".Liquid", liquidName);
    compoundTag.putInt(DimStorage.ID + ".Amount", liquidLevel);
  }

  @Override
  public ResourceLocation getUid() {
    return DimStorage.rl("dim_tank");
  }
}
