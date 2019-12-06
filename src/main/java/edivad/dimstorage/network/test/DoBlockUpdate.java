package edivad.dimstorage.network.test;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.container.ContainerDimChest;
import edivad.dimstorage.tile.TileEntityDimChest;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class DoBlockUpdate implements IMessage {

	private BlockPos pos;
	private Frequency freq;
	private boolean locked;

	public DoBlockUpdate()
	{
	}

	public DoBlockUpdate(TileEntityDimChest tile)
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

	public static class Handler implements IMessageHandler<DoBlockUpdate, IMessage> {

		@Override
		public IMessage onMessage(DoBlockUpdate msg, MessageContext ctx)
		{

			FMLClientHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(msg, ctx));
			return null;
		}

		private void handle(DoBlockUpdate msg, MessageContext ctx)
		{
			if(ctx.side == Side.SERVER)
			{
				TileEntity tile = ctx.getServerHandler().player.world.getTileEntity(msg.pos);
				if(!(tile instanceof TileEntityDimChest))
				{
					Main.logger.error("Wrong type of tile entity (expected TileEntityDimChest)!");
					return;
				}
				TileEntityDimChest chest = (TileEntityDimChest) tile;
				chest.frequency.set(msg.freq);
				chest.locked = msg.locked;
				//                chest.reloadStorage();
				if(ctx.getServerHandler().player.openContainer instanceof ContainerDimChest)
				{
					ContainerDimChest containerChest = (ContainerDimChest) ctx.getServerHandler().player.openContainer;
					//Close all the open chest with the old frequency, and open the new chest with the new one
					containerChest.chestInv.closeInventory();
					containerChest.chestInv = chest.getStorage();
					containerChest.chestInv.openInventory();
					containerChest.detectAndSendChanges();
				}

				ctx.getServerHandler().player.world.markBlockRangeForRenderUpdate(msg.pos, msg.pos);
			}
		}
	}
}
