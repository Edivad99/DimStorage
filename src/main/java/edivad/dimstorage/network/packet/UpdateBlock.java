package edivad.dimstorage.network.packet;

import java.util.function.Supplier;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.tile.TileFrequencyOwner;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

public class UpdateBlock {

	private BlockPos pos;
	private Frequency freq;
	private boolean locked;

	public UpdateBlock(PacketBuffer buf)
	{
		pos = buf.readBlockPos();
		freq = new Frequency(buf.readString(), buf.readInt());
		locked = buf.readBoolean();
	}

	public UpdateBlock(TileFrequencyOwner tile)
	{
		pos = tile.getPos();
		freq = tile.frequency;
		locked = tile.isLocked();
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

			if(!(tile instanceof TileFrequencyOwner))
			{
				Main.logger.error("Wrong type of tile entity (expected TileFrequencyOwner)!");
				return;
			}

			TileFrequencyOwner te = (TileFrequencyOwner) tile;
			te.frequency.set(freq);
			te.setLocked(locked);

			world.notifyBlockUpdate(pos, te.getBlockState(), te.getBlockState(), 3);
			if(te.canAccess())
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, buf -> buf.writeBlockPos(pos).writeBoolean(true));
		});
		ctx.get().setPacketHandled(true);
	}
}
