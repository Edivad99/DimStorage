package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.IServerDataProvider;

public class DimBlockBaseProvider implements IServerDataProvider<BlockEntity> {

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity tileEntity, boolean showDetails) {
        if(tileEntity instanceof BlockEntityFrequencyOwner blockentity) {
            Frequency frequency = blockentity.getFrequency();
            compoundTag.putBoolean(Main.MODID + ".HasOwner", frequency.hasOwner());
            compoundTag.putBoolean(Main.MODID + ".CanAccess", blockentity.canAccess(serverPlayer));
            compoundTag.putString(Main.MODID + ".Owner", frequency.getOwner());
            compoundTag.putInt(Main.MODID + ".Frequency", frequency.getChannel());
            compoundTag.putBoolean(Main.MODID + ".Locked", blockentity.locked);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Main.MODID, "dim_block_base");
    }
}