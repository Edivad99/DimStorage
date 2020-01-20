package edivad.dimstorage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.proxy.IProxy;
import edivad.dimstorage.proxy.Proxy;
import edivad.dimstorage.proxy.ProxyClient;
import edivad.dimstorage.tabs.DimStorageTab;
import edivad.dimstorage.tools.Config;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Main.MODID)
public class Main {

	public static final String MODID = "dimstorage";
	public static final String MODNAME = "DimStorage";

	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ProxyClient(), () -> () -> new Proxy());

	public static final ItemGroup dimStorageTab = new DimStorageTab(Main.MODID + "_tab");

	public static final Logger logger = LogManager.getLogger();

	public Main()
	{
		//ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

		//Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-client.toml"));
		Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-common.toml"));
	}

	public static MinecraftServer getServer()
	{
		return LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
	}

	private void setup(final FMLCommonSetupEvent event)
	{
		proxy.init();
	}

	@SubscribeEvent
	public void preServerStart(FMLServerStartedEvent event)
	{
		DimStorageManager.reloadManager(false);
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

		@SubscribeEvent
		public static void onBlockRegistry(final RegistryEvent.Register<Block> event)
		{
			ModBlocks.register(event.getRegistry());
		}

		@SubscribeEvent
		public static void onItemsRegistry(final RegistryEvent.Register<Item> event)
		{
			ModItems.register(event.getRegistry());
		}

		@SubscribeEvent
		public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event)
		{
			ModBlocks.registerTiles(event.getRegistry());
		}

		@SubscribeEvent
		public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event)
		{
			ModBlocks.registerContainers(event.getRegistry());
		}
	}
}
