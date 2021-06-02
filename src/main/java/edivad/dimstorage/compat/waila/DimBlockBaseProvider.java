package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
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
            compoundNBT.putBoolean(Main.MODID + ".HasOwner", frequency.hasOwner());
            compoundNBT.putBoolean(Main.MODID + ".CanAccess", tile.canAccess(serverPlayerEntity));
            compoundNBT.putString(Main.MODID + ".Owner", frequency.getOwner());
            compoundNBT.putInt(Main.MODID + ".Frequency", frequency.getChannel());
            compoundNBT.putBoolean(Main.MODID + ".Locked", tile.locked);
        }
    }
}
