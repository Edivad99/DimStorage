package edivad.dimstorage.network;

import edivad.dimstorage.Main;
import edivad.dimstorage.network.packet.OpenChest;
import edivad.dimstorage.network.packet.UpdateBlock;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

	public static final SimpleNetworkWrapper packetReq = NetworkRegistry.INSTANCE.newSimpleChannel(Main.MODID + "2");

	public static void init()
	{
		int id = 0;
		packetReq.registerMessage(UpdateBlock.class, UpdateBlock.class, id++, Side.SERVER);
		packetReq.registerMessage(OpenChest.class, OpenChest.class, id++, Side.CLIENT);
	};
}
