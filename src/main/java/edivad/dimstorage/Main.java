package edivad.dimstorage;

import org.apache.logging.log4j.Logger;

import codechicken.lib.CodeChickenLib;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.proxy.Proxy;
import edivad.dimstorage.tabs.DimStorageTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.MODVERSION, dependencies = Main.DEPENDENCIES, acceptedMinecraftVersions = Main.MCVERSION, updateJSON = Main.UPDATE_URL, useMetadata = true)
public class Main {

	public static final String MODID = "dimstorage";
	public static final String MODNAME = "DimStorage";
	public static final String MCVERSION = "1.12.2";
	public static final String MODVERSION = "1.2.1";
	public static final String DEPENDENCIES = "required-after:forge@[14.23.5,);" + CodeChickenLib.MOD_VERSION_DEP;
	public static final String UPDATE_URL = "https://raw.githubusercontent.com/Edivad99/mod-version-controll/master/dimstorage_update.json";
	public static final String CLIENT_PROXY_CLASS = "edivad.dimstorage.proxy.ProxyClient";
	public static final String SERVER_PROXY_CLASS = "edivad.dimstorage.proxy.Proxy";

	@Instance(Main.MODID)
	public static Main instance;

	@SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = SERVER_PROXY_CLASS)
	public static Proxy proxy;

	public static Configuration config;

	public static CreativeTabs tabDimStorage = new DimStorageTab(Main.MODID);

	public static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void preServerStart(FMLServerStartedEvent event)
	{
		DimStorageManager.reloadManager(false);
	}
}
