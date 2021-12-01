package edivad.dimstorage;

import edivad.dimstorage.setup.ClientSetup;
import edivad.dimstorage.setup.ModSetup;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.setup.proxy.IProxy;
import edivad.dimstorage.setup.proxy.Proxy;
import edivad.dimstorage.setup.proxy.ProxyClient;
import edivad.dimstorage.tools.Config;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//import net.minecraftforge.fmllegacy.LogicalSidedProvider;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Main.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Main {

    public static final String MODID = "dimstorage";
    public static final String MODNAME = "DimStorage";

    public static IProxy proxy = DistExecutor.safeRunForDist(() -> ProxyClient::new, () -> Proxy::new);

    public static final Logger logger = LogManager.getLogger();

    public Main() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
        Registration.init();

        // Register the setup method for modloading
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(ModSetup::init);
        modBus.addListener(ClientSetup::init);
    }

    public static MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
        //return LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
    }
}
