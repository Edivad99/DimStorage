package edivad.dimstorage.network;

import codechicken.lib.packet.ICustomPacketHandler.IServerPacketHandler;
import codechicken.lib.packet.PacketCustom;
import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.INetHandlerPlayServer;

public class DimStorageSPH implements IServerPacketHandler {

	public static final String channel = Main.MODID;

	@Override
	public void handlePacket(PacketCustom packet, EntityPlayerMP sender, INetHandlerPlayServer handler)
	{

	}

	public static void sendOpenUpdateTo(EntityPlayer player, Frequency freq, boolean open)
	{
		PacketCustom packet = new PacketCustom(channel, 3);
		//packet.writeString(owner);
		freq.writeToPacket(packet);
		packet.writeBoolean(open);

		packet.sendToPlayer(player);
	}
}
