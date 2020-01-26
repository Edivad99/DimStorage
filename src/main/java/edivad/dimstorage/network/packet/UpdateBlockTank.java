package edivad.dimstorage.network.packet;

import java.util.function.Supplier;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.tile.TileEntityDimTank;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

public class UpdateBlockTank {

	private BlockPos pos;
	private Frequency freq;
	private boolean locked;

	public UpdateBlockTank(PacketBuffer buf)
	{
		pos = buf.readBlockPos();
		freq = new Frequency(buf.readString(), buf.readInt());
		locked = buf.readBoolean();
	}

	public UpdateBlockTank(TileEntityDimTank tile)
	{
		pos = tile.getPos();
		freq = tile.frequency;
		locked = tile.locked;
	}

	public void toBytes(PacketBuffer buf)
	{
		buf.writeBlockPos(pos);

		buf.writeString(freq.getOwner());
		buf.writeInt(freq.getChannel());

		buf.writeBoolean(locked);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			PlayerEntity player = ctx.get().getSender();
			World world = player.world;
			if(!world.isBlockPresent(pos))
				return;

			TileEntity tile = world.getTileEntity(pos);

			if(!(tile instanceof TileEntityDimTank))
			{
				Main.logger.error("Wrong type of tile entity (expected TileEntityDimTank)!");
				return;
			}

			TileEntityDimTank tank = (TileEntityDimTank) tile;
			tank.frequency.set(freq);
			tank.locked = locked;

			world.notifyBlockUpdate(pos, tank.getBlockState(), tank.getBlockState(), 3);
			if(tank.canAccess())
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tank, buf -> buf.writeBlockPos(pos).writeBoolean(true));
		});
		ctx.get().setPacketHandled(true);
	}
}
