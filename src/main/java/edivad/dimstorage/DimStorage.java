package edivad.dimstorage;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import edivad.dimstorage.blockentities.BlockEntityDimChest;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.client.screen.ScreenDimChest;
import edivad.dimstorage.client.screen.ScreenDimTablet;
import edivad.dimstorage.client.screen.ScreenDimTank;
import edivad.dimstorage.compat.top.TOPProvider;
import edivad.dimstorage.datagen.DimStorageAdvancementProvider;
import edivad.dimstorage.datagen.DimStorageLootTableProvider;
import edivad.dimstorage.datagen.Lang;
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
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@Mod(DimStorage.ID)
public class DimStorage {

  public static final String ID = "dimstorage";
  public static final String MODNAME = "DimStorage";

  public static final Logger LOGGER = LogUtils.getLogger();

  public DimStorage(IEventBus modEventBus, Dist dist) {

    Registration.init(modEventBus);
    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleClientSetup);
    modEventBus.addListener(this::handleRegisterMenuScreens);
    modEventBus.addListener(this::handleGatherData);
    modEventBus.addListener(this::registerCapabilities);
    var packetHandler = new PacketHandler(modEventBus);
    DimStorageCreativeModeTabs.register(modEventBus);
    Config.init();

    if (dist.isClient()) {
      ClientSetup.init(modEventBus);
    }

    NeoForge.EVENT_BUS.addListener(this::registerCommands);
    NeoForge.EVENT_BUS.addListener(this::handleServerStarted);
  }

  private void handleCommonSetup(FMLCommonSetupEvent event) {
    DimStorageManager.registerPlugin(new DimChestPlugin());
    DimStorageManager.registerPlugin(new DimTankPlugin());
    NeoForge.EVENT_BUS.register(new DimStorageManager.DimStorageSaveHandler());

    //Register TheOneProbe
    if (ModList.get().isLoaded("theoneprobe")) {
      InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPProvider::new);
    }
  }

  private void handleClientSetup(FMLClientSetupEvent event) {
    NeoForge.EVENT_BUS.register(new UpdateChecker(ID));
  }

  private void handleRegisterMenuScreens(RegisterMenuScreensEvent event) {
    event.register(Registration.DIMCHEST_MENU.get(), ScreenDimChest::new);
    event.register(Registration.DIMTABLET_MENU.get(), ScreenDimTablet::new);
    event.register(Registration.DIMTANK_MENU.get(), ScreenDimTank::new);
  }

  private void handleGatherData(GatherDataEvent event) {
    var generator = event.getGenerator();
    var packOutput = generator.getPackOutput();
    var lookupProvider = event.getLookupProvider();
    var fileHelper = event.getExistingFileHelper();

    generator.addProvider(event.includeServer(), new Recipes(packOutput));
    generator.addProvider(event.includeServer(), new DimStorageLootTableProvider(packOutput));
    generator.addProvider(event.includeServer(),
        new TagsProvider(packOutput, lookupProvider, fileHelper));
    generator.addProvider(event.includeServer(),
        new DimStorageAdvancementProvider(packOutput, lookupProvider, fileHelper));
    generator.addProvider(event.includeClient(), new Lang(packOutput));
  }

  private void registerCommands(RegisterCommandsEvent event) {
    DimCommands.init(event.getDispatcher());
  }

  private void handleServerStarted(ServerStartedEvent event) {
    DimStorageManager.reloadManager(false);
  }

  private void registerCapabilities(RegisterCapabilitiesEvent event) {
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Registration.DIMCHEST_TILE.get(),
        BlockEntityDimChest::getItemHandler);
    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, Registration.DIMTANK_TILE.get(),
        BlockEntityDimTank::getFluidHandler);
  }

  public static ResourceLocation rl(String path) {
    return new ResourceLocation(ID, path);
  }
}
