package edivad.dimstorage.network.packet;

import edivad.dimstorage.Main;
import edivad.dimstorage.tile.TileEntityDimTank;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.fml.network.NetworkHooks;

public class UpdateDimTank extends UpdateDimBase {

	protected boolean autoEject;

	public UpdateDimTank(PacketBuffer buf)
	{
		super(buf);
		autoEject = buf.readBoolean();
	}

	public UpdateDimTank(TileEntityDimTank tile)
	{
		super(tile);
		autoEject = tile.autoEject;
	}

	@Override
	public void toBytes(PacketBuffer buf)
	{
		super.toBytes(buf);
		buf.writeBoolean(autoEject);
	}

	@Override
	public void customHandle(World world, PlayerEntity player)
	{
		TileEntity tile = world.getTileEntity(pos);

		if(!(tile instanceof TileEntityDimTank))
		{
			Main.logger.error("Wrong type of tile entity (expected TileEntityDimTank)!");
			return;
		}

		TileEntityDimTank tank = (TileEntityDimTank) tile;
		tank.setFreq(freq);
		tank.locked = locked;
		tank.autoEject = autoEject;
		tank.markDirty();

		world.notifyBlockUpdate(pos, tank.getBlockState(), tank.getBlockState(), BlockFlags.DEFAULT);
		NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tank, buf -> buf.writeBlockPos(pos).writeBoolean(true));
	}
}
