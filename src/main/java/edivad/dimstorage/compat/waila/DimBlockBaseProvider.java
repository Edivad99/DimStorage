package edivad.dimstorage.compat.waila;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.tile.TileFrequencyOwner;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class DimBlockBaseProvider implements IServerDataProvider<TileEntity> {

    @Override
    public void appendServerData(CompoundNBT compoundNBT, ServerPlayerEntity serverPlayerEntity, World world, TileEntity tileEntity)
    {
        if(tileEntity instanceof TileFrequencyOwner)
        {
            TileFrequencyOwner tile = (TileFrequencyOwner) tileEntity;
            Frequency frequency = tile.getFrequency();
            compoundNBT.putBoolean("HasOwner", frequency.hasOwner());
            compoundNBT.putBoolean("CanAccess", tile.canAccess(serverPlayerEntity));
            compoundNBT.putString("Owner", frequency.getOwner());
            compoundNBT.putInt("Frequency", frequency.getChannel());
            compoundNBT.putBoolean("Locked", tile.locked);
        }
    }
}
