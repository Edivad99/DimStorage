package edivad.dimstorage.network.packet;

import java.util.function.Supplier;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class UpdateBlock  {

	private BlockPos pos;
	private Frequency freq;
	private boolean locked;

	public UpdateBlock(PacketBuffer buf)
	{
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		freq = new Frequency(buf.readString(), buf.readInt());
		locked = buf.readBoolean();
	}

	public UpdateBlock(TileEntityDimChest tile)
	{
		pos = tile.getPos();
		freq = tile.frequency;
		locked = tile.locked;
	}

	public void toBytes(PacketBuffer buf)
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		
		buf.writeString(freq.getOwner());
		buf.writeInt(freq.getChannel());
		buf.writeBoolean(locked);
	}

	
	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			World world = player.world;
			if(!world.isBlockLoaded(pos))
				return;
			
			TileEntity tile = world.getTileEntity(pos);
			
			if(!(tile instanceof TileEntityDimChest))
			{
				Main.logger.error("Wrong type of tile entity (expected TileEntityDimChest)!");
				return;
			}
			
			TileEntityDimChest chest = (TileEntityDimChest) tile;
			chest.frequency.set(freq);
			chest.locked = locked;
			
			world.notifyBlockUpdate(pos, chest.getBlockState(), chest.getBlockState(), 3);
//			if(chest.canAccess())
//				player.openGui(Main.MODID, 1, world, pos.getX(), pos.getY(), pos.getZ());
		});
		ctx.get().setPacketHandled(true);
	}
}
