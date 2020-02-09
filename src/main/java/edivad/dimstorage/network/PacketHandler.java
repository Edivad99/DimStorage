package edivad.dimstorage.network;

import edivad.dimstorage.Main;
import edivad.dimstorage.network.packet.OpenChest;
import edivad.dimstorage.network.packet.UpdateBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

	private static final String PROTOCOL_VERSION = "1";
	// @formatter:off
	public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
								new ResourceLocation(Main.MODID, "net"), 
								() -> PROTOCOL_VERSION, 
								PROTOCOL_VERSION::equals, 
								PROTOCOL_VERSION::equals);
	// @formatter:on
	public static void init()
	{
		int id = 0;
		INSTANCE.registerMessage(id++, UpdateBlock.class, UpdateBlock::toBytes, UpdateBlock::new, UpdateBlock::handle);
		INSTANCE.registerMessage(id++, OpenChest.class, OpenChest::toBytes, OpenChest::new, OpenChest::handle);
	};
}
