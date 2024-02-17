package edivad.dimstorage.compat.waila;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public class DimBlockBaseProvider implements IServerDataProvider<BlockAccessor> {

  @Override
  public void appendServerData(CompoundTag compoundTag, BlockAccessor accessor) {
    BlockEntityFrequencyOwner blockEntity = (BlockEntityFrequencyOwner) accessor.getBlockEntity();
    Player player = accessor.getPlayer();
    Frequency frequency = blockEntity.getFrequency();
    compoundTag.putBoolean(DimStorage.ID + ".HasOwner", frequency.hasOwner());
    compoundTag.putBoolean(DimStorage.ID + ".CanAccess", blockEntity.canAccess(player));
    compoundTag.putString(DimStorage.ID + ".Owner", frequency.getOwner());
    compoundTag.putInt(DimStorage.ID + ".Frequency", frequency.getChannel());
    compoundTag.putBoolean(DimStorage.ID + ".Locked", blockEntity.locked);
  }

  @Override
  public ResourceLocation getUid() {
    return DimStorage.rl("dim_block_base");
  }
}
