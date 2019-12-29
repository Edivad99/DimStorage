package edivad.dimstorage.proxy;

import com.google.common.util.concurrent.ListenableFuture;

import edivad.dimstorage.Main;
import edivad.dimstorage.ModBlocks;
import edivad.dimstorage.ModItems;
import edivad.dimstorage.compat.MainCompatHandler;
import edivad.dimstorage.compat.opencomputers.DriverDimChest;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.plugin.DimChestPlugin;
import li.cil.oc.api.Driver;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod.EventBusSubscriber
public class Proxy {

	public void preInit(FMLPreInitializationEvent e)
	{
		DimStorageManager.registerPlugin(new DimChestPlugin());
		MinecraftForge.EVENT_BUS.register(new DimStorageManager.DimStorageSaveHandler());
		MainCompatHandler.registerTOP();
		MainCompatHandler.registerWaila();
	}

	public void init(FMLInitializationEvent e)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());
		PacketHandler.init();

		//OpenComputers
		if(Loader.isModLoaded("opencomputers"))
		{
			Driver.add(new DriverDimChest());
		}
	}

	public void postInit(FMLPostInitializationEvent e)
	{

	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		ModBlocks.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		ModItems.register(event.getRegistry());
	}

	public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule)
	{
		throw new IllegalStateException("This should only be called from client side");
	}

	public EntityPlayer getClientPlayer()
	{
		throw new IllegalStateException("This should only be called from client side");
	}
}
