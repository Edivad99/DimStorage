package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
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
        compoundTag.putBoolean(Main.MODID + ".HasOwner", frequency.hasOwner());
        compoundTag.putBoolean(Main.MODID + ".CanAccess", blockEntity.canAccess(player));
        compoundTag.putString(Main.MODID + ".Owner", frequency.getOwner());
        compoundTag.putInt(Main.MODID + ".Frequency", frequency.getChannel());
        compoundTag.putBoolean(Main.MODID + ".Locked", blockEntity.locked);
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Main.MODID, "dim_block_base");
    }
}
