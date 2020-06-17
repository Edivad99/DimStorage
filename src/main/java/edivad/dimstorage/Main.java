package edivad.dimstorage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edivad.dimstorage.setup.ClientSetup;
import edivad.dimstorage.setup.ModSetup;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.setup.proxy.IProxy;
import edivad.dimstorage.setup.proxy.Proxy;
import edivad.dimstorage.setup.proxy.ProxyClient;
import edivad.dimstorage.tools.Config;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Main {

	public static final String MODID = "dimstorage";
	public static final String MODNAME = "DimStorage";

	public static IProxy proxy = DistExecutor.safeRunForDist(() -> ProxyClient::new, () -> Proxy::new);

	public static final Logger logger = LogManager.getLogger();

	public Main()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
		Registration.init();

		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
	}

	public static MinecraftServer getServer()
	{
		return LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
	}
}
