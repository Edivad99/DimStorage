package edivad.dimstorage.compat.opencomputers;

import edivad.dimstorage.tile.TileEntityDimChest;
import li.cil.oc.api.driver.DriverBlock;
import li.cil.oc.api.network.ManagedEnvironment;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverDimChest implements DriverBlock {

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing)
	{
		TileEntityDimChest tile = (TileEntityDimChest) world.getTileEntity(pos);

		return new EnvironmentDimChest(tile);
	}

	@Override
	public boolean worksWith(World world, BlockPos pos, EnumFacing facing)
	{
		TileEntity tile = world.getTileEntity(pos);

		return tile != null && tile instanceof TileEntityDimChest;
	}

}
