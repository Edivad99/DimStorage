package edivad.dimstorage.network.test;

import edivad.dimstorage.Main;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

	public static final SimpleNetworkWrapper packetReq = NetworkRegistry.INSTANCE.newSimpleChannel(Main.MODID + "2");

	public static void init()
	{
		packetReq.registerMessage(DoBlockUpdate.Handler.class, DoBlockUpdate.class, 0, Side.SERVER);
	};
}
