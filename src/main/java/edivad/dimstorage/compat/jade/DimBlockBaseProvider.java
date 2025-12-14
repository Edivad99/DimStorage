package edivad.dimstorage.compat.jade;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public class DimBlockBaseProvider implements IServerDataProvider<BlockAccessor> {

  @Override
  public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
    if (accessor.getBlockEntity() instanceof BlockEntityFrequencyOwner blockEntity) {
      var player = accessor.getPlayer();
      var frequency = blockEntity.getFrequency();
      tag.putBoolean("has_owner", frequency.hasOwner());
      tag.putBoolean("can_access", blockEntity.canAccess(player));
      tag.putString("gameProfile", frequency.getOwner());
      tag.putInt("frequency", frequency.channel());
      tag.putBoolean("locked", blockEntity.isLocked());
    }
  }

  @Override
  public ResourceLocation getUid() {
    return DimStorage.rl("dim_block_base");
  }
}
