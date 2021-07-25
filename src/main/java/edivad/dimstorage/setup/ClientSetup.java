package edivad.dimstorage.setup;

import edivad.dimstorage.client.render.tile.RenderTileDimChest;
import edivad.dimstorage.client.render.tile.RenderTileDimTank;
import edivad.dimstorage.client.screen.ScreenDimChest;
import edivad.dimstorage.client.screen.ScreenDimTablet;
import edivad.dimstorage.client.screen.ScreenDimTank;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void init(FMLClientSetupEvent event)
    {
        //Version checker
        MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);

        //Special render & GUI

        ClientRegistry.bindTileEntityRenderer(Registration.DIMCHEST_TILE.get(), RenderTileDimChest::new);
        ClientRegistry.bindTileEntityRenderer(Registration.DIMTANK_TILE.get(), RenderTileDimTank::new);
        ItemBlockRenderTypes.setRenderLayer(Registration.DIMTANK.get(), RenderType.cutout());

        MenuScreens.register(Registration.DIMCHEST_CONTAINER.get(), ScreenDimChest::new);
        MenuScreens.register(Registration.DIMTABLET_CONTAINER.get(), ScreenDimTablet::new);
        MenuScreens.register(Registration.DIMTANK_CONTAINER.get(), ScreenDimTank::new);
    }
}