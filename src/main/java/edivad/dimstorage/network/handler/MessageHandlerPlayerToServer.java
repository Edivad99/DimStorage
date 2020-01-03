//package edivad.dimstorage.network.handler;
//
//import javax.xml.ws.handler.MessageContext;
//
//import net.minecraft.world.World;
//
//public abstract class MessageHandlerPlayerToServer<T extends IMessage> implements IMessageHandler<T, IMessage> {
//
//	@Override
//	public IMessage onMessage(T msg, MessageContext ctx)
//	{
//		final EntityPlayerMP player = ctx.getServerHandler().player;
//		final World world = player.world;
//
//		FMLClientHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(msg, world, player));
//		return null;
//	}
//
//	protected abstract void handle(T msg, World world, EntityPlayerMP player);
//}
