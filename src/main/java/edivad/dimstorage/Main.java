package edivad.dimstorage;

import com.mojang.logging.LogUtils;
import edivad.dimstorage.setup.ClientSetup;
import edivad.dimstorage.setup.ModSetup;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.tools.Config;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Main.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Main {

    public static final String MODID = "dimstorage";
    public static final String MODNAME = "DimStorage";

    public static final Logger LOGGER = LogUtils.getLogger();

    public Main() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
        Registration.init();

        // Register the setup method for modloading
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(ModSetup::init);
        modBus.addListener(ClientSetup::init);
    }
}
