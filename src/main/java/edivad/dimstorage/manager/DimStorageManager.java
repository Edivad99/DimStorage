package edivad.dimstorage.manager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.DimStoragePlugin;
import edivad.dimstorage.api.Frequency;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DimStorageManager {

	public static class DimStorageSaveHandler {

		@SubscribeEvent
		public void onWorldLoad(Load event)
		{
			if(event.getWorld().getWorld().isRemote)
				reloadManager(true);
		}

		@SubscribeEvent
		public void onWorldSave(Save event)
		{
			if(!event.getWorld().getWorld().isRemote && instance(false) != null)
				instance(false).save(false);
		}

		@SubscribeEvent
		public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
		{
			instance(false).sendClientInfo(event.getPlayer());
		}

		@SubscribeEvent
		public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
		{
			instance(false).sendClientInfo(event.getPlayer());
		}
	}
	
	//private static MinecraftServer minecraftServer;
	private static DimStorageManager serverManager;
	private static DimStorageManager clientManager;
	private static HashMap<String, DimStoragePlugin> plugins = new HashMap<>();

	private Map<String, AbstractDimStorage> storageMap;
	private Map<String, List<AbstractDimStorage>> storageList;
	public final boolean client;

	private File saveDir;
	private File[] saveFiles;
	private int saveTo;
	private List<AbstractDimStorage> dirtyStorage;
	private CompoundNBT saveTag;

	public DimStorageManager(boolean client)
	{
		this.client = client;

		storageMap = Collections.synchronizedMap(new HashMap<String, AbstractDimStorage>());
		storageList = Collections.synchronizedMap(new HashMap<String, List<AbstractDimStorage>>());
		dirtyStorage = Collections.synchronizedList(new LinkedList<AbstractDimStorage>());

		for(String key : plugins.keySet())
			this.storageList.put(key, new ArrayList<AbstractDimStorage>());

		if(!client)
			load();
			
	}

	private void sendClientInfo(PlayerEntity player)
	{
		for(Map.Entry<String, DimStoragePlugin> plugin : plugins.entrySet())
		{
			plugin.getValue().sendClientInfo(player, storageList.get(plugin.getKey()));
		}
	}

	private void load()
	{
		ServerWorld serverWorld = null;/*DimensionManager.getWorld(minecraftServer, DimensionType.OVERWORLD, false, false);*/
		if(serverWorld == null)
			return;
		
		this.saveDir = new File(serverWorld.getSaveHandler().getWorldDirectory(), "DimStorage");
		
		try
		{
			if(!this.saveDir.exists())
				this.saveDir.mkdirs();

			this.saveFiles = new File [] { new File(saveDir, "data1.dat"), new File(saveDir, "data2.dat"), new File(saveDir, "lock.dat") };

			if(this.saveFiles[2].exists() && this.saveFiles[2].length() > 0)
			{
				FileInputStream fin = new FileInputStream(this.saveFiles[2]);
				this.saveTo = fin.read() ^ 1;
				fin.close();

				if(this.saveFiles[this.saveTo ^ 1].exists())
				{
					DataInputStream din = new DataInputStream(new FileInputStream(this.saveFiles[this.saveTo ^ 1]));
					this.saveTag = CompressedStreamTools.readCompressed(din);
					din.close();
				}
				else
					saveTag = new CompoundNBT();
			}
			else
				saveTag = new CompoundNBT();
		}
		catch(Exception e)
		{
			throw new RuntimeException(String.format("DimStorage was unable to read it's data, please delete the 'DimStorage' folder Here: %s and start the server again.", saveDir), e);
		}
	}

	private void save(boolean force)
	{
		if(!this.dirtyStorage.isEmpty() || force)
		{
			for(AbstractDimStorage inv : this.dirtyStorage)
			{
				saveTag.put(inv.freq + ",type=" + inv.type(), inv.saveToTag());
				inv.setClean();
			}

			dirtyStorage.clear();

			try
			{
				File saveFile = saveFiles[saveTo];
				if(!saveFile.exists())
					saveFile.createNewFile();

				DataOutputStream dout = new DataOutputStream(new FileOutputStream(saveFile));
				CompressedStreamTools.writeCompressed(saveTag, dout);
				dout.close();
				FileOutputStream fout = new FileOutputStream(saveFiles[2]);
				fout.write(saveTo);
				fout.close();
				saveTo ^= 1;
			}
			catch(Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public static void reloadManager(boolean client)
	{
		DimStorageManager newManager = new DimStorageManager(client);

		if(client)
			clientManager = newManager;
		else
			serverManager = newManager;
			
	}

	public File getSaveDir()
	{
		return saveDir;
	}

	public static DimStorageManager instance(boolean client)
	{
		DimStorageManager manager = client ? clientManager : serverManager;
		if(manager == null)
		{
			reloadManager(client);
			manager = client ? clientManager : serverManager;
		}
		return manager;
	}

	public AbstractDimStorage getStorage(Frequency freq, String type)
	{
		String key = freq + ",type=" + type;
		AbstractDimStorage storage = storageMap.get(key);

		if(storage == null)
		{
			storage = plugins.get(type).createDimStorage(this, freq);

			if(!client && saveTag.contains(key))
				storage.loadFromTag(saveTag.getCompound(key));

			storageMap.put(key, storage);
			storageList.get(type).add(storage);
		}
		return storage;
	}

	public static void registerPlugin(DimStoragePlugin plugin)
	{
		plugins.put(plugin.identifer(), plugin);

		if(serverManager != null)
			serverManager.storageList.put(plugin.identifer(), new ArrayList<AbstractDimStorage>());
		if(clientManager != null)
			clientManager.storageList.put(plugin.identifer(), new ArrayList<AbstractDimStorage>());
	}

	public static DimStoragePlugin getPlugin(String identifier)
	{
		return plugins.get(identifier);
	}

	public static Map<String, DimStoragePlugin> getPlugins()
	{
		return ImmutableMap.copyOf(plugins);
	}

	public void requestSave(AbstractDimStorage storage)
	{
		dirtyStorage.add(storage);
	}
}
