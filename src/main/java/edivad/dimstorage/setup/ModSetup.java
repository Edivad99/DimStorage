package edivad.dimstorage.setup;

import edivad.dimstorage.Main;
import edivad.dimstorage.compat.top.TOPProvider;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.plugin.DimChestPlugin;
import edivad.dimstorage.plugin.DimTankPlugin;
import edivad.dimstorage.tools.DimCommands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static void init(final FMLCommonSetupEvent event) {
        PacketHandler.init();
        DimStorageManager.registerPlugin(new DimChestPlugin());
        DimStorageManager.registerPlugin(new DimTankPlugin());
        MinecraftForge.EVENT_BUS.register(new DimStorageManager.DimStorageSaveHandler());

        //Register TheOneProbe
        if(ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPProvider::new);
        }
    }

    @SubscribeEvent
    public static void preServerStart(final ServerStartedEvent event) {
        DimStorageManager.reloadManager(false);
    }

    @SubscribeEvent
    public static void registerCommands(final RegisterCommandsEvent event) {
        DimCommands.init(event.getDispatcher());
    }
}
