//package edivad.dimstorage.tools;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import com.google.common.collect.ImmutableMap;
//
//import edivad.dimstorage.Main;
//import edivad.dimstorage.api.AbstractDimStorage;
//import edivad.dimstorage.api.DimStoragePlugin;
//import edivad.dimstorage.api.Frequency;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.world.World;
//import net.minecraft.world.dimension.DimensionType;
//import net.minecraft.world.server.ServerWorld;
//import net.minecraft.world.storage.WorldSavedData;
//import net.minecraftforge.event.entity.player.PlayerEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//
//public class DimStorageManager extends WorldSavedData {
//
//	public static class DimStorageSaveHandler {
//
//		@SubscribeEvent
//		public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
//		{
//			getData(event.getEntity().world).sendClientInfo(event.getPlayer());
//		}
//
//		@SubscribeEvent
//		public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
//		{
//			getData(event.getEntity().world).sendClientInfo(event.getPlayer());
//		}
//	}
//	
//	private static final String NAME = Main.MODNAME;
//	
//	private static HashMap<String, DimStoragePlugin> plugins = new HashMap<>();
//	private Map<String, AbstractDimStorage> storageMap;
//	private Map<String, List<AbstractDimStorage>> storageList;
//	
//	private List<AbstractDimStorage> dirtyStorage;
//	private CompoundNBT saveTag;
//
//	// Required constructors
//	public DimStorageManager(String name)
//	{
//		super(name);
//		storageMap = Collections.synchronizedMap(new HashMap<String, AbstractDimStorage>());
//		storageList = Collections.synchronizedMap(new HashMap<String, List<AbstractDimStorage>>());
//		dirtyStorage = Collections.synchronizedList(new LinkedList<AbstractDimStorage>());
//		
//		for(String key : plugins.keySet())
//			this.storageList.put(key, new ArrayList<AbstractDimStorage>());
//	}
//
//	public DimStorageManager()
//	{
//		this(NAME);
//	}
//	
//	private void sendClientInfo(PlayerEntity player)
//	{
//		for(Map.Entry<String, DimStoragePlugin> plugin : plugins.entrySet())
//		{
//			plugin.getValue().sendClientInfo(player, storageList.get(plugin.getKey()));
//		}
//	}
//	
//	public AbstractDimStorage getStorage(Frequency freq, String type)
//	{
//		String key = freq + ",type=" + type;
//		AbstractDimStorage storage = storageMap.get(key);
//
//		if(storage == null)
//		{
//			//storage = plugins.get(type).createDimStorage(this, freq);
//
//			if(saveTag.contains(key))
//				storage.loadFromTag(saveTag.getCompound(key));
//
//			storageMap.put(key, storage);
//			storageList.get(type).add(storage);
//		}
//		return storage;
//	}
//	
//	
//	public static void registerPlugin(World world, DimStoragePlugin plugin)
//	{
//		plugins.put(plugin.identifer(), plugin);
//
//		DimStorageManager manager = getData(world);
//		if(manager != null)
//			manager.storageList.put(plugin.identifer(), new ArrayList<AbstractDimStorage>());
//	}
//	
//    public static DimStorageManager getData(World world) {
//        if (world instanceof ServerWorld) 
//        {
//            ServerWorld serverWorld = ((ServerWorld) world).getServer().getWorld(DimensionType.OVERWORLD);
//            return serverWorld.getSavedData().getOrCreate(DimStorageManager::new, NAME);
//        }
//        return null;
//    }
//	
////    public static DimStorageManager get(World world)
////	{
////		// The IS_GLOBAL constant is there for clarity, and should be simplified into the right branch.
////		MapData storage = world.getMapData(NAME);
////		DimStorageManager instance = (DimStorageManager) storage.getOrLoadData(DimStorageManager.class, NAME);
////
////		if(instance == null)
////		{
////			instance = new DimStorageManager();
////			storage.setData(NAME, instance);
////		}
////		return instance;
////	}
//    
//	public static DimStoragePlugin getPlugin(String identifier)
//	{
//		return plugins.get(identifier);
//	}
//
//	public static Map<String, DimStoragePlugin> getPlugins()
//	{
//		return ImmutableMap.copyOf(plugins);
//	}
//	
//	public void requestSave(AbstractDimStorage storage)
//	{
//		dirtyStorage.add(storage);
//	}
//
//	@Override
//	public void read(CompoundNBT nbt)
//	{
//		dirtyStorage.clear();
//		saveTag = nbt.getCompound(NAME);
//	}
//
//	@Override
//	public CompoundNBT write(CompoundNBT compound)
//	{
//		CompoundNBT saveTag = new CompoundNBT();
//		for(AbstractDimStorage inv : dirtyStorage)
//		{
//			saveTag.put(inv.toString(), inv.saveToTag());
//			inv.setClean();
//		}
//		dirtyStorage.clear();
//		compound.put(NAME, saveTag);
//		return compound;
//	}
//}
