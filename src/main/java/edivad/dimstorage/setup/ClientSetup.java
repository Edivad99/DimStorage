package edivad.dimstorage.setup;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.render.tile.RenderTileDimChest;
import edivad.dimstorage.client.screen.ScreenDimChest;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

	public static void init(final FMLClientSetupEvent event)
	{
		//Version checker
		MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);

		//Special render & GUI
		ClientRegistry.bindTileEntityRenderer(Registration.DIMCHEST_TILE.get(), RenderTileDimChest::new);
		ScreenManager.registerFactory(Registration.DIMCHEST_CONTAINER.get(), ScreenDimChest::new);
	}
}