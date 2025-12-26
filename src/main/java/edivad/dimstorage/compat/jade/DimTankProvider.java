package edivad.dimstorage.compat.jade;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentity.BlockEntityDimTank;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;

public class DimTankProvider extends DimBlockBaseProvider {

  @Override
  public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
    super.appendServerData(tag, accessor);
    if (accessor.getBlockEntity() instanceof BlockEntityDimTank blockEntity) {
      tag.putBoolean("auto_eject", blockEntity.autoEject);
    }
  }

  @Override
  public Identifier getUid() {
    return DimStorage.id("dim_tank");
  }
}
