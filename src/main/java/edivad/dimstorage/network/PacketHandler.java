package edivad.dimstorage.network;

import edivad.dimstorage.Main;
import edivad.dimstorage.network.packet.OpenChest;
import edivad.dimstorage.network.packet.UpdateDimChest;
import edivad.dimstorage.network.packet.tank.PlayerItemTankCacheSync;
import edivad.dimstorage.network.packet.tank.SyncLiquidTank;
import edivad.dimstorage.network.packet.tank.UpdateDimTank;
import edivad.dimstorage.network.packet.tank.UpdateTankLiquid;
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
		INSTANCE.registerMessage(id++, UpdateDimChest.class, UpdateDimChest::toBytes, UpdateDimChest::new, UpdateDimChest::handle);
		INSTANCE.registerMessage(id++, OpenChest.class, OpenChest::toBytes, OpenChest::new, OpenChest::handle);

		INSTANCE.registerMessage(id++, UpdateDimTank.class, UpdateDimTank::toBytes, UpdateDimTank::new, UpdateDimTank::handle);
		INSTANCE.registerMessage(id++, UpdateTankLiquid.class, UpdateTankLiquid::toBytes, UpdateTankLiquid::new, UpdateTankLiquid::handle);
		INSTANCE.registerMessage(id++, PlayerItemTankCacheSync.class, PlayerItemTankCacheSync::toBytes, PlayerItemTankCacheSync::new, PlayerItemTankCacheSync::handle);
		INSTANCE.registerMessage(id++, SyncLiquidTank.class, SyncLiquidTank::toBytes, SyncLiquidTank::new, SyncLiquidTank::handle);
	};
}
