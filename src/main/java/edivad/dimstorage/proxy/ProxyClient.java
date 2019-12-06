package edivad.dimstorage.proxy;

import com.google.common.util.concurrent.ListenableFuture;

import codechicken.lib.packet.PacketCustom;
import edivad.dimstorage.ModBlocks;
import edivad.dimstorage.ModItems;
import edivad.dimstorage.network.DimStorageCPH;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ProxyClient extends Proxy {

	@Override
	public void preInit(FMLPreInitializationEvent e)
	{
		super.preInit(e);
		MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);
	}

	@Override
	public void init(FMLInitializationEvent e)
	{
		super.init(e);
		PacketCustom.assignHandler(DimStorageCPH.channel, new DimStorageCPH());
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		ModBlocks.initModels();
		ModItems.initModels();
	}

	@Override
	public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule)
	{
		return Minecraft.getMinecraft().addScheduledTask(runnableToSchedule);
	}

	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().player;
	}

}
