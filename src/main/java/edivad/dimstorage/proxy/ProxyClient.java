package edivad.dimstorage.proxy;

import edivad.dimstorage.client.render.tile.RenderTileDimChest;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ProxyClient implements IProxy  {
	
	@Override
	public void init() {
//		ScreenManager.registerFactory(ModBlocks.containerDimChest, ScreenDimChest::new);
		MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDimChest.class, new RenderTileDimChest());
	}

//	@Override
//	public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule)
//	{
//		return Minecraft.getMinecraft().addScheduledTask(runnableToSchedule);
//	}

	@Override
	public PlayerEntity getClientPlayer()
	{
		return Minecraft.getInstance().player;
	}

	@Override
	public World getClientWorld()
	{
		return Minecraft.getInstance().world;
	}

}
