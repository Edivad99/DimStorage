//package edivad.dimstorage.network.handler;
//
//import javax.xml.ws.handler.MessageContext;
//
//import edivad.dimstorage.Main;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
//import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
//
//public abstract class MessageHandlerServerToPlayer<T extends IMessage> implements IMessageHandler<T, IMessage> {
//
//	@Override
//	public IMessage onMessage(T msg, MessageContext ctx)
//	{
//		final EntityPlayer player = Main.proxy.getClientPlayer();
//		final World world = player.world;
//
//		Main.proxy.addScheduledTaskClient(() -> handle(msg, world, player));
//		return null;
//	}
//
//	protected abstract void handle(T msg, World world, EntityPlayer player);
//}