package edivad.dimstorage;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import edivad.dimstorage.blockentity.BlockEntityDimChest;
import edivad.dimstorage.blockentity.BlockEntityDimTank;
import edivad.dimstorage.client.screen.ScreenDimChest;
import edivad.dimstorage.client.screen.ScreenDimTablet;
import edivad.dimstorage.client.screen.ScreenDimTank;
import edivad.dimstorage.datagen.DimStorageAdvancementProvider;
import edivad.dimstorage.datagen.DimStorageLanguageProvider;
import edivad.dimstorage.datagen.DimStorageRecipeProvider;
import edivad.dimstorage.datagen.DimStorageTagsProvider;
import edivad.dimstorage.datagen.loot.DimStorageLootTableProvider;
import edivad.dimstorage.datagen.models.DimStorageModelProvider;
import edivad.dimstorage.items.components.DimStorageComponents;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.to_client.OpenChest;
import edivad.dimstorage.network.to_client.SyncLiquidTank;
import edivad.dimstorage.network.to_server.UpdateDimChest;
import edivad.dimstorage.network.to_server.UpdateDimTank;
import edivad.dimstorage.plugin.DimChestPlugin;
import edivad.dimstorage.plugin.DimTankPlugin;
import edivad.dimstorage.setup.ClientSetup;
import edivad.dimstorage.setup.Config;
import edivad.dimstorage.setup.DimStorageCreativeModeTabs;
import edivad.dimstorage.setup.ModRegistration;
import edivad.dimstorage.tools.DimCommands;
import edivad.edivadlib.setup.UpdateChecker;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
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
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(DimStorage.ID)
public class DimStorage {

  public static final String ID = "dimstorage";
  public static final String MODNAME = "DimStorage";

  public static final Logger LOGGER = LogUtils.getLogger();

  public DimStorage(ModContainer modContainer, Dist dist) {

    var modEventBus = modContainer.getEventBus();
    ModRegistration.init(modEventBus);
    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleClientSetup);
    modEventBus.addListener(this::handleRegisterMenuScreens);
    modEventBus.addListener(this::handleGatherData);
    modEventBus.addListener(this::registerCapabilities);
    modEventBus.addListener(this::registerPayloads);
    DimStorageCreativeModeTabs.register(modEventBus);
    DimStorageComponents.register(modEventBus);
    Config.registerConfig(modContainer);

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
//    if (ModList.get().isLoaded("theoneprobe")) {
//      InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPProvider::new);
//    }
  }

  private void handleClientSetup(FMLClientSetupEvent event) {
    NeoForge.EVENT_BUS.register(new UpdateChecker(ID));
  }

  private void handleRegisterMenuScreens(RegisterMenuScreensEvent event) {
    event.register(ModRegistration.DIMCHEST_MENU.get(), ScreenDimChest::new);
    event.register(ModRegistration.DIMTABLET_MENU.get(), ScreenDimTablet::new);
    event.register(ModRegistration.DIMTANK_MENU.get(), ScreenDimTank::new);
  }

  private void handleGatherData(GatherDataEvent.Client event) {
    event.createProvider(DimStorageRecipeProvider.Runner::new);
    event.createProvider(DimStorageLootTableProvider::new);
    event.createProvider(DimStorageTagsProvider::new);
    event.createProvider(DimStorageAdvancementProvider::new);
    event.createProvider(DimStorageLanguageProvider::new);
    event.createProvider(DimStorageModelProvider::new);
  }

  private void registerCommands(RegisterCommandsEvent event) {
    DimCommands.init(event.getDispatcher());
  }

  private void handleServerStarted(ServerStartedEvent event) {
    DimStorageManager.reloadManager(event.getServer().overworld());
  }

  private void registerCapabilities(RegisterCapabilitiesEvent event) {
    event.registerBlockEntity(Capabilities.Item.BLOCK, ModRegistration.DIMCHEST_TILE.get(),
        BlockEntityDimChest::getItemHandler);
    event.registerBlockEntity(Capabilities.Fluid.BLOCK, ModRegistration.DIMTANK_TILE.get(),
        BlockEntityDimTank::getFluidHandler);
  }

  private void registerPayloads(RegisterPayloadHandlersEvent event) {
    var registrar = event.registrar(ID).versioned("1");
    registrar.playToClient(SyncLiquidTank.TYPE, SyncLiquidTank.STREAM_CODEC, SyncLiquidTank::handle);
    registrar.playToClient(OpenChest.TYPE, OpenChest.STREAM_CODEC, OpenChest::handle);

    registrar.playToServer(UpdateDimChest.TYPE, UpdateDimChest.STREAM_CODEC, UpdateDimChest::handle);
    registrar.playToServer(UpdateDimTank.TYPE, UpdateDimTank.STREAM_CODEC, UpdateDimTank::handle);
  }

  public static Identifier id(String path) {
    return Identifier.fromNamespaceAndPath(ID, path);
  }
}
