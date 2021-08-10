package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DimBlockBaseProvider implements IServerDataProvider<BlockEntity> {

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity tileEntity, boolean showDetails) {
        if(tileEntity instanceof BlockEntityFrequencyOwner tile) {
            Frequency frequency = tile.getFrequency();
            compoundTag.putBoolean(Main.MODID + ".HasOwner", frequency.hasOwner());
            compoundTag.putBoolean(Main.MODID + ".CanAccess", tile.canAccess(serverPlayer));
            compoundTag.putString(Main.MODID + ".Owner", frequency.getOwner());
            compoundTag.putInt(Main.MODID + ".Frequency", frequency.getChannel());
            compoundTag.putBoolean(Main.MODID + ".Locked", tile.locked);
        }
    }
}
