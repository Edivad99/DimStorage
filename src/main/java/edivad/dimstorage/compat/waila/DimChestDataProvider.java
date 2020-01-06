package edivad.dimstorage.compat.waila;

import edivad.dimstorage.tile.TileEntityDimChest;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class DimChestDataProvider implements IServerDataProvider<TileEntity> {

	@Override
	public void appendServerData(CompoundNBT compoundNBT, ServerPlayerEntity serverPlayerEntity, World world, TileEntity tileEntity)
	{
		if(tileEntity instanceof TileEntityDimChest)
		{
			TileEntityDimChest tile = (TileEntityDimChest) tileEntity;
			compoundNBT.putBoolean("HasOwner", tile.frequency.hasOwner());
			compoundNBT.putBoolean("CanAccess", tile.canAccess());
			compoundNBT.putString("Owner", tile.frequency.getOwner());
			compoundNBT.putInt("Frequency", tile.frequency.getChannel());
			compoundNBT.putBoolean("Locked", tile.locked);
		}
	}
}