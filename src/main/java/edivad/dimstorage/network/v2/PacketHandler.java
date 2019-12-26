package edivad.dimstorage.network.v2;

import edivad.dimstorage.Main;
import edivad.dimstorage.network.v2.server.UpdateBlock;
import edivad.dimstorage.network.v2.server.UpdateOpen;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

	public static final SimpleNetworkWrapper packetReq = NetworkRegistry.INSTANCE.newSimpleChannel(Main.MODID + "2");
	
	public static void init()
	{
		int id = 0;
		packetReq.registerMessage(UpdateBlock.class, UpdateBlock.class, id++, Side.SERVER);
	};
}
