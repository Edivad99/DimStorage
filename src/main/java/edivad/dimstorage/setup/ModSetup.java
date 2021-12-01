package edivad.dimstorage.setup;

import edivad.dimstorage.Main;
import edivad.dimstorage.compat.MainCompatHandler;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.plugin.DimChestPlugin;
import edivad.dimstorage.plugin.DimTankPlugin;
import edivad.dimstorage.tools.DimCommands;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartedEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final CreativeModeTab dimStorageTab = new CreativeModeTab(Main.MODID + "_tab") {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registration.DIMCHEST.get());
        }
    };

    public static void init(final FMLCommonSetupEvent event) {
        PacketHandler.init();
        DimStorageManager.registerPlugin(new DimChestPlugin());
        DimStorageManager.registerPlugin(new DimTankPlugin());
        MinecraftForge.EVENT_BUS.register(new DimStorageManager.DimStorageSaveHandler());

        //Compat
        MainCompatHandler.registerTOP();
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
