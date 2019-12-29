package edivad.dimstorage.network.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class MessageHandlerPlayerToServer <T extends IMessage> implements IMessageHandler<T, IMessage> {

	@Override
	public IMessage onMessage(T msg, MessageContext ctx)
	{
		final EntityPlayerMP player = ctx.getServerHandler().player;
		final World world = player.world;
		
		FMLClientHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(msg, world, player));
		return null;
	}
	
	protected abstract void handle(T msg, World world, EntityPlayerMP player);
}
