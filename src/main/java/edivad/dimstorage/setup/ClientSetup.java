package edivad.dimstorage.setup;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.render.blockentity.DimChestRenderer;
import edivad.dimstorage.client.render.blockentity.DimTankRenderer;
import edivad.dimstorage.client.screen.ScreenDimChest;
import edivad.dimstorage.client.screen.ScreenDimTablet;
import edivad.dimstorage.client.screen.ScreenDimTank;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(FMLClientSetupEvent event) {
        //Version checker
        MinecraftForge.EVENT_BUS.register(new EventHandler());

        //Special render & GUI
        ItemBlockRenderTypes.setRenderLayer(Registration.DIMTANK.get(), RenderType.cutout());

        MenuScreens.register(Registration.DIMCHEST_CONTAINER.get(), ScreenDimChest::new);
        MenuScreens.register(Registration.DIMTABLET_CONTAINER.get(), ScreenDimTablet::new);
        MenuScreens.register(Registration.DIMTANK_CONTAINER.get(), ScreenDimTank::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DimChestRenderer.STATIC_LAYER, DimChestRenderer::createStaticLayer);
        event.registerLayerDefinition(DimChestRenderer.MOVABLE_LAYER, DimChestRenderer::createMovableLayer);
        event.registerLayerDefinition(DimChestRenderer.GREEN_INDICATOR_LAYER, () -> DimChestRenderer.createIndicatorLayer(0));
        event.registerLayerDefinition(DimChestRenderer.BLUE_INDICATOR_LAYER, () -> DimChestRenderer.createIndicatorLayer(2));
        event.registerLayerDefinition(DimChestRenderer.RED_INDICATOR_LAYER, () -> DimChestRenderer.createIndicatorLayer(4));
    }

    @SubscribeEvent
    public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(Registration.DIMCHEST_TILE.get(), DimChestRenderer::new);
        event.registerBlockEntityRenderer(Registration.DIMTANK_TILE.get(), DimTankRenderer::new);
    }
}