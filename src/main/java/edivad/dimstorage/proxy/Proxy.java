package edivad.dimstorage.proxy;

import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.plugin.DimChestPlugin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class Proxy implements IProxy {

	@Override
	public void init()
	{
		DimStorageManager.registerPlugin(new DimChestPlugin());
		MinecraftForge.EVENT_BUS.register(new DimStorageManager.DimStorageSaveHandler());
		PacketHandler.init();
	}

	@Override
	public PlayerEntity getClientPlayer()
	{
		throw new IllegalStateException("This should only be called from client side");
	}

	@Override
	public World getClientWorld()
	{
		throw new IllegalStateException("This should only be called from client side");
	}
}
