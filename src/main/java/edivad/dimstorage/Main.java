package edivad.dimstorage;

import com.mojang.logging.LogUtils;
import edivad.dimstorage.setup.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Main.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Main {

    public static final String MODID = "dimstorage";
    public static final String MODNAME = "DimStorage";

    public static final Logger LOGGER = LogUtils.getLogger();

    public Main() {
        Registration.init();
        Config.init();

        // Register the setup method for modloading
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ModSetup::init);
        modEventBus.addListener(ClientSetup::init);
        DimStorageCreativeModeTabs.register(modEventBus);
    }
}
