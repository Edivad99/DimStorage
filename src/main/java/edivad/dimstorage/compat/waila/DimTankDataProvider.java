package edivad.dimstorage.compat.waila;

import edivad.dimstorage.tile.TileEntityDimTank;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class DimTankDataProvider implements IServerDataProvider<TileEntity> {

	@Override
	public void appendServerData(CompoundNBT compoundNBT, ServerPlayerEntity serverPlayerEntity, World world, TileEntity tileEntity)
	{
		if(tileEntity instanceof TileEntityDimTank)
		{
			TileEntityDimTank tile = (TileEntityDimTank) tileEntity;
			compoundNBT.putBoolean("HasOwner", tile.frequency.hasOwner());
			compoundNBT.putBoolean("CanAccess", tile.canAccess(serverPlayerEntity));
			compoundNBT.putString("Owner", tile.frequency.getOwner());
			compoundNBT.putInt("Frequency", tile.frequency.getChannel());
			compoundNBT.putBoolean("Locked", tile.locked);
			compoundNBT.putBoolean("AutoEject", tile.autoEject);

			String liquidName = tile.liquidState.serverLiquid.getDisplayName().getFormattedText();
			int liquidLevel = tile.liquidState.serverLiquid.getAmount();
			compoundNBT.putString("Liquid", liquidName);
			compoundNBT.putInt("Amount", liquidLevel);
		}
	}
}