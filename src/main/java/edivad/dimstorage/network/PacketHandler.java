package edivad.dimstorage.network;

import edivad.dimstorage.Main;
import edivad.dimstorage.network.packet.OpenChest;
import edivad.dimstorage.network.packet.UpdateBlock;
import edivad.dimstorage.network.packet.UpdateBlockTank;
import edivad.dimstorage.network.packet.tank.PlayerItemTankCacheSync;
import edivad.dimstorage.network.packet.tank.SyncLiquidTank;
import edivad.dimstorage.network.packet.tank.UpdateTankLiquid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

	public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Main.MODID, "net"), () -> "1.0", s -> true, s -> true);

	public static void init()
	{
		int id = 0;
		INSTANCE.registerMessage(id++, UpdateBlock.class, UpdateBlock::toBytes, UpdateBlock::new, UpdateBlock::handle);
		INSTANCE.registerMessage(id++, OpenChest.class, OpenChest::toBytes, OpenChest::new, OpenChest::handle);
		INSTANCE.registerMessage(id++, UpdateBlockTank.class, UpdateBlockTank::toBytes, UpdateBlockTank::new, UpdateBlockTank::handle);

		INSTANCE.registerMessage(id++, UpdateTankLiquid.class, UpdateTankLiquid::toBytes, UpdateTankLiquid::new, UpdateTankLiquid::handle);
		INSTANCE.registerMessage(id++, PlayerItemTankCacheSync.class, PlayerItemTankCacheSync::toBytes, PlayerItemTankCacheSync::new, PlayerItemTankCacheSync::handle);
		INSTANCE.registerMessage(id++, SyncLiquidTank.class, SyncLiquidTank::toBytes, SyncLiquidTank::new, SyncLiquidTank::handle);
	};
}
