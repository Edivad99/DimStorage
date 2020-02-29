package edivad.dimstorage.setup;

import edivad.dimstorage.Main;
import edivad.dimstorage.compat.MainCompatHandler;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.plugin.DimChestPlugin;
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
		MinecraftForge.EVENT_BUS.register(new DimStorageManager.DimStorageSaveHandler());

		//Compat
		MainCompatHandler.registerTOP();
		//MainCompatHandler.registerOC();
	}

	@SubscribeEvent
	public static void preServerStart(final FMLServerStartedEvent event)
	{
		DimStorageManager.reloadManager(false);
	}
}
