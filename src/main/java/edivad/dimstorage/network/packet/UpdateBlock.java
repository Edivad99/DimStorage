package edivad.dimstorage.network.packet;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.network.handler.MessageHandlerPlayerToServer;
import edivad.dimstorage.tile.TileEntityDimChest;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UpdateBlock extends MessageHandlerPlayerToServer<UpdateBlock> implements IMessage {

	private BlockPos pos;
	private Frequency freq;
	private boolean locked;

	public UpdateBlock()
	{
	}

	public UpdateBlock(TileEntityDimChest tile)
	{
		pos = tile.getPos();
		freq = tile.frequency;
		locked = tile.locked;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		freq = new Frequency(ByteBufUtils.readUTF8String(buf), buf.readInt());
		locked = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());

		ByteBufUtils.writeUTF8String(buf, freq.getOwner());
		buf.writeInt(freq.getChannel());
		buf.writeBoolean(locked);
	}

	@Override
	public void handle(UpdateBlock msg, World world, EntityPlayerMP player)
	{
		TileEntity tile = world.getTileEntity(msg.pos);

		if(!(tile instanceof TileEntityDimChest))
		{
			Main.logger.error("Wrong type of tile entity (expected TileEntityDimChest)!");
			return;
		}
		
		TileEntityDimChest chest = (TileEntityDimChest) tile;
		chest.frequency.set(msg.freq);
		chest.locked = msg.locked;

		world.markBlockRangeForRenderUpdate(msg.pos, msg.pos);
		if(chest.canAccess())
			player.openGui(Main.MODID, 1, world, msg.pos.getX(), msg.pos.getY(), msg.pos.getZ());
	}

}
