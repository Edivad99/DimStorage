package edivad.dimstorage.setup;

import edivad.dimstorage.Main;
import edivad.dimstorage.compat.MainCompatHandler;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.TankSynchroniser;
import edivad.dimstorage.plugin.DimChestPlugin;
import edivad.dimstorage.plugin.DimTankPlugin;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

	public static final ItemGroup dimStorageTab = new ItemGroup(Main.MODID + "_tab") {

		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(Registration.DIMCHEST.get());
		}
	};

	public static void init(final FMLCommonSetupEvent event)
	{
		PacketHandler.init();
		DimStorageManager.registerPlugin(new DimChestPlugin());
		DimStorageManager.registerPlugin(new DimTankPlugin());
		MinecraftForge.EVENT_BUS.register(new DimStorageManager.DimStorageSaveHandler());
		MinecraftForge.EVENT_BUS.register(new TankSynchroniser());

		//Compat
		MainCompatHandler.registerTOP();
		//OpenComputers
		//if(Loader.isModLoaded("opencomputers"))
		//{
		//	Driver.add(new DriverDimChest());
		//}
	}

	@SubscribeEvent
	public static void preServerStart(final FMLServerStartedEvent event)
	{
		DimStorageManager.reloadManager(false);
	}
}
