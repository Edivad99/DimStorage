package edivad.dimstorage.setup;

import edivad.dimstorage.client.render.blockentity.DimChestRenderer;
import edivad.dimstorage.client.render.blockentity.DimTankRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ClientSetup {

  public static void init(IEventBus modEventBus) {
    modEventBus.addListener(ClientSetup::registerLayerDefinitions);
    modEventBus.addListener(ClientSetup::registerRenders);
  }

  private static void registerLayerDefinitions(
      EntityRenderersEvent.RegisterLayerDefinitions event) {
    event.registerLayerDefinition(DimChestRenderer.STATIC_LAYER,
        DimChestRenderer::createStaticLayer);
    event.registerLayerDefinition(DimChestRenderer.MOVABLE_LAYER,
        DimChestRenderer::createMovableLayer);
    event.registerLayerDefinition(DimChestRenderer.GREEN_INDICATOR_LAYER,
        () -> DimChestRenderer.createIndicatorLayer(0));
    event.registerLayerDefinition(DimChestRenderer.BLUE_INDICATOR_LAYER,
        () -> DimChestRenderer.createIndicatorLayer(2));
    event.registerLayerDefinition(DimChestRenderer.RED_INDICATOR_LAYER,
        () -> DimChestRenderer.createIndicatorLayer(4));
  }

  private static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(Registration.DIMCHEST_TILE.get(), DimChestRenderer::new);
    event.registerBlockEntityRenderer(Registration.DIMTANK_TILE.get(), DimTankRenderer::new);
  }
}