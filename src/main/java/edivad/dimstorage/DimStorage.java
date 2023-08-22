package edivad.dimstorage;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import edivad.dimstorage.client.screen.ScreenDimChest;
import edivad.dimstorage.client.screen.ScreenDimTablet;
import edivad.dimstorage.client.screen.ScreenDimTank;
import edivad.dimstorage.compat.top.TOPProvider;
import edivad.dimstorage.datagen.AdvancementProvider;
import edivad.dimstorage.datagen.Lang;
import edivad.dimstorage.datagen.LootTables;
import edivad.dimstorage.datagen.Recipes;
import edivad.dimstorage.datagen.TagsProvider;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.plugin.DimChestPlugin;
import edivad.dimstorage.plugin.DimTankPlugin;
import edivad.dimstorage.setup.ClientSetup;
import edivad.dimstorage.setup.Config;
import edivad.dimstorage.setup.DimStorageCreativeModeTabs;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.tools.DimCommands;
import edivad.edivadlib.setup.UpdateChecker;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(DimStorage.ID)
public class DimStorage {

  public static final String ID = "dimstorage";
  public static final String MODNAME = "DimStorage";

  public static final Logger LOGGER = LogUtils.getLogger();

  public DimStorage() {
    var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    Registration.init(modEventBus);
    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleClientSetup);
    modEventBus.addListener(this::handleGatherData);
    DimStorageCreativeModeTabs.register(modEventBus);
    Config.init();

    if (FMLEnvironment.dist == Dist.CLIENT) {
      ClientSetup.init(modEventBus);
    }

    MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    MinecraftForge.EVENT_BUS.addListener(this::handleServerStarted);
  }

  private void handleCommonSetup(FMLCommonSetupEvent event) {
    PacketHandler.init();
    DimStorageManager.registerPlugin(new DimChestPlugin());
    DimStorageManager.registerPlugin(new DimTankPlugin());
    MinecraftForge.EVENT_BUS.register(new DimStorageManager.DimStorageSaveHandler());

    //Register TheOneProbe
    if (ModList.get().isLoaded("theoneprobe")) {
      InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPProvider::new);
    }
  }

  private void handleClientSetup(FMLClientSetupEvent event) {
    MinecraftForge.EVENT_BUS.register(new UpdateChecker(DimStorage.ID));
    MenuScreens.register(Registration.DIMCHEST_CONTAINER.get(), ScreenDimChest::new);
    MenuScreens.register(Registration.DIMTABLET_CONTAINER.get(), ScreenDimTablet::new);
    MenuScreens.register(Registration.DIMTANK_CONTAINER.get(), ScreenDimTank::new);
  }

  private void handleGatherData(GatherDataEvent event) {
    var generator = event.getGenerator();
    var packOutput = generator.getPackOutput();
    var lookupProvider = event.getLookupProvider();
    var fileHelper = event.getExistingFileHelper();

    generator.addProvider(event.includeServer(), new Recipes(packOutput));
    generator.addProvider(event.includeServer(),
        (DataProvider.Factory<LootTableProvider>) LootTables::create);
    generator.addProvider(event.includeServer(), new TagsProvider(packOutput, lookupProvider,
        fileHelper));
    generator.addProvider(event.includeServer(), new AdvancementProvider(packOutput,
        lookupProvider, fileHelper));
    generator.addProvider(event.includeClient(), new Lang(packOutput));
  }

  private void registerCommands(RegisterCommandsEvent event) {
    DimCommands.init(event.getDispatcher());
  }

  private void handleServerStarted(ServerStartedEvent event) {
    DimStorageManager.reloadManager(false);
  }
}
