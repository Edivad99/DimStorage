package edivad.dimstorage.network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.common.collect.Sets;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.packet.tank.PlayerItemTankCacheSync;
import edivad.dimstorage.network.packet.tank.UpdateTankLiquid;
import edivad.dimstorage.storage.DimTankStorage;
import edivad.dimstorage.tools.extra.MathHelper;
import edivad.dimstorage.tools.extra.fluid.FluidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.PacketDistributor;

public class TankSynchroniser {

	public static abstract class TankState {

		public Frequency frequency;
		public FluidStack c_liquid = new FluidStack(Fluids.WATER, 0);
		public FluidStack s_liquid = new FluidStack(Fluids.WATER, 0);
		public FluidStack f_liquid = new FluidStack(Fluids.WATER, 0);

		public void setFrequency(Frequency frequency)
		{
			this.frequency = frequency;
		}

		public void update(boolean client)
		{
			FluidStack b_liquid;
			FluidStack a_liquid;
			if(client)
			{
				b_liquid = c_liquid.copy();

				if(s_liquid.isFluidEqual(c_liquid))
				{
					c_liquid.setAmount(MathHelper.approachExpI(c_liquid.getAmount(), s_liquid.getAmount(), 0.1));
				}
				else if(c_liquid.getAmount() > 100)
				{
					c_liquid.setAmount(MathHelper.retreatExpI(c_liquid.getAmount(), 0, f_liquid.getAmount(), 0.1, 1000));
				}
				else
				{
					c_liquid = FluidUtils.copy(s_liquid, 0);
				}

				a_liquid = c_liquid;
			}
			else
			{
				s_liquid = getStorage(false).getFluidInTank(0);
				b_liquid = s_liquid.copy();
				if(!s_liquid.isFluidEqual(c_liquid))
				{
					sendSyncPacket();
					c_liquid = s_liquid;
				}
				else if(Math.abs(c_liquid.getAmount() - s_liquid.getAmount()) > 250 || (s_liquid.getAmount() == 0 && c_liquid.getAmount() > 0))
				{
					// Diff grater than 250 Or server no longer has liquid and client does.
					sendSyncPacket();
					c_liquid = s_liquid;
				}

				a_liquid = s_liquid;
			}
			if((b_liquid.getAmount() == 0) != (a_liquid.getAmount() == 0) || !b_liquid.isFluidEqual(a_liquid))
			{
				onLiquidChanged();
			}
		}

		public void onLiquidChanged()
		{
		}

		public abstract void sendSyncPacket();

		public void sync(FluidStack liquid)
		{
			s_liquid = liquid;
			if(!s_liquid.isFluidEqual(c_liquid))
			{
				f_liquid = c_liquid.copy();
			}
		}

		//SERVER SIDE ONLY!
		public DimTankStorage getStorage(boolean client)
		{
			return (DimTankStorage) DimStorageManager.instance(client).getStorage(frequency, "fluid");
		}
	}

	public static class PlayerItemTankState extends TankState {

		private PlayerEntity player;
		private boolean tracking;

		public PlayerItemTankState(PlayerEntity player, DimTankStorage storage)
		{
			this.player = player;
			setFrequency(storage.freq);
			tracking = true;
		}

		public PlayerItemTankState()
		{
		}

		@Override
		public void sendSyncPacket()
		{
			if(!tracking)
			{
				return;
			}

			PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new UpdateTankLiquid(getStorage(false).freq, s_liquid));
		}

		public void setTracking(boolean t)
		{
			tracking = t;
		}

		@Override
		public void update(boolean client)
		{
			if(tracking || client)
			{
				super.update(client);
			}
		}
	}

	public static class PlayerItemTankCache {

		private boolean client;
		private HashMap<String, PlayerItemTankState> tankStates = new HashMap<>();
		//client
		private HashSet<Frequency> b_visible;
		private HashSet<Frequency> a_visible;
		//server
		private PlayerEntity player;

		public PlayerItemTankCache(PlayerEntity player)
		{
			this.player = player;
			client = false;
		}

		public PlayerItemTankCache()
		{
			client = true;
			a_visible = new HashSet<>();
			b_visible = new HashSet<>();
		}

		public void track(Frequency freq, boolean t)
		{
			String key = freq.toString();
			PlayerItemTankState state = tankStates.get(key);
			if(state == null)
			{
				if(!t)
				{
					return;
				}
				tankStates.put(key, state = new PlayerItemTankState(player, (DimTankStorage) DimStorageManager.instance(false).getStorage(freq, "fluid")));
			}
			state.setTracking(t);
		}

		public void sync(Frequency freq, FluidStack liquid)
		{
			String key = freq.toString();
			PlayerItemTankState state = tankStates.computeIfAbsent(key, k -> new PlayerItemTankState());
			state.sync(liquid);
		}

		public void update()
		{
			for(Map.Entry<String, PlayerItemTankState> entry : tankStates.entrySet())
			{
				entry.getValue().update(client);
			}

			if(client)
			{
				Sets.SetView<Frequency> new_visible = Sets.difference(a_visible, b_visible);
				Sets.SetView<Frequency> old_visible = Sets.difference(b_visible, a_visible);

				if(!new_visible.isEmpty() || !old_visible.isEmpty())
				{
					PacketHandler.INSTANCE.sendToServer(new PlayerItemTankCacheSync(new_visible, old_visible));
				}

				HashSet<Frequency> temp = b_visible;
				temp.clear();
				b_visible = a_visible;
				a_visible = temp;
			}
		}

		public FluidStack getLiquid(Frequency freq)
		{
			String key = freq.toString();
			a_visible.add(freq);
			PlayerItemTankState state = tankStates.get(key);
			return state == null ? new FluidStack(Fluids.WATER, 0) : state.c_liquid;
		}

		public void handleVisiblityPacket(Frequency[] new_visible, Frequency[] old_visible)
		{
			for(int i = 0; i < new_visible.length; i++)
			{
				track(new_visible[i], true);
			}
			for(int i = 0; i < old_visible.length; i++)
			{
				track(old_visible[i], false);
			}
		}
	}

	private static HashMap<String, PlayerItemTankCache> playerItemTankStates;
	private static PlayerItemTankCache clientState;

	public static void syncClient(Frequency freq, FluidStack liquid)
	{
		clientState.sync(freq, liquid);
	}

	public static FluidStack getClientLiquid(Frequency freq)
	{
		if(clientState != null)
		{
			return clientState.getLiquid(freq);
		}
		return new FluidStack(Fluids.WATER, 0);
	}

	public static void handleVisiblityPacket(PlayerEntity player, Frequency[] new_visible, Frequency[] old_visible)//Da chiamare lato server
	{
		playerItemTankStates.get(player.getName().getFormattedText()).handleVisiblityPacket(new_visible, old_visible);
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		playerItemTankStates.put(event.getPlayer().getName().getFormattedText(), new PlayerItemTankCache((PlayerEntity) event.getPlayer()));
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
	{
		if(playerItemTankStates != null)
		{
			//sometimes world unloads before players logout
			playerItemTankStates.remove(event.getPlayer().getName().getFormattedText());
		}
	}

	@SubscribeEvent
	public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
	{
		playerItemTankStates.put(event.getPlayer().getName().getFormattedText(), new PlayerItemTankCache((PlayerEntity) event.getPlayer()));
	}

	@SubscribeEvent
	public void tickEnd(TickEvent.ServerTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END && playerItemTankStates != null)
		{
			for(Map.Entry<String, PlayerItemTankCache> entry : playerItemTankStates.entrySet())
			{
				entry.getValue().update();
			}
		}
	}

	@SubscribeEvent
	public void tickEnd(TickEvent.ClientTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
		{
			if(Minecraft.getInstance().getConnection() != null && clientState != null)
			{
				clientState.update();
			}
		}
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event)
	{
		if(!event.getWorld().isRemote() && !Main.getServer().isServerRunning())
		{
			playerItemTankStates = null;
		}
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		if(event.getWorld().isRemote())
		{
			clientState = new PlayerItemTankCache();
		}
		else if(playerItemTankStates == null)
		{
			playerItemTankStates = new HashMap<>();
		}
	}
}
