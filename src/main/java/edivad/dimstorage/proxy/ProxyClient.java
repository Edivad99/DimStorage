package edivad.dimstorage.proxy;

import edivad.dimstorage.ModBlocks;
import edivad.dimstorage.client.render.tile.RenderTileDimChest;
import edivad.dimstorage.client.screen.ScreenDimChest;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.plugin.DimChestPlugin;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ProxyClient implements IProxy  {
	
	@Override
	public void init() {
		
		DimStorageManager.registerPlugin(new DimChestPlugin());
		MinecraftForge.EVENT_BUS.register(new DimStorageManager.DimStorageSaveHandler());
		MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDimChest.class, new RenderTileDimChest());
		ScreenManager.registerFactory(ModBlocks.containerDimChest, ScreenDimChest::new);
//		MainCompatHandler.registerTOP();
//		MainCompatHandler.registerWaila();
		//NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());
		PacketHandler.init();

		//OpenComputers
		//if(Loader.isModLoaded("opencomputers"))
		//{
		//	Driver.add(new DriverDimChest());
		//}
	}

	@Override
	public PlayerEntity getClientPlayer()
	{
		return Minecraft.getInstance().player;
	}

	@Override
	public World getClientWorld()
	{
		return Minecraft.getInstance().world;
	}

}
