package edivad.dimstorage.network;

import java.util.Optional;

import edivad.dimstorage.Main;
import edivad.dimstorage.network.packet.OpenChest;
import edivad.dimstorage.network.packet.SyncLiquidTank;
import edivad.dimstorage.network.packet.UpdateDimChest;
import edivad.dimstorage.network.packet.UpdateDimTank;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
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
		INSTANCE.registerMessage(id++, UpdateDimChest.class, UpdateDimChest::toBytes, UpdateDimChest::new, UpdateDimChest::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(id++, OpenChest.class, OpenChest::toBytes, OpenChest::new, OpenChest::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

		INSTANCE.registerMessage(id++, UpdateDimTank.class, UpdateDimTank::toBytes, UpdateDimTank::new, UpdateDimTank::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(id++, SyncLiquidTank.class, SyncLiquidTank::toBytes, SyncLiquidTank::new, SyncLiquidTank::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	};
}
