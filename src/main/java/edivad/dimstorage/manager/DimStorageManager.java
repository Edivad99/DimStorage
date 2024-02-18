package edivad.dimstorage.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import edivad.dimstorage.DimStorage;
import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.DimStoragePlugin;
import edivad.dimstorage.api.Frequency;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

public class DimStorageManager extends SavedData {

  private static final String DATA_TAG = "dimstorage.inventories";
  private static final HashMap<String, DimStoragePlugin> PLUGINS = new HashMap<>();
  private static DimStorageManager SERVER_MANAGER;
  private static DimStorageManager CLIENT_MANAGER;
  private final boolean client;
  private final Map<String, AbstractDimStorage> storageMap;
  private final Map<String, List<AbstractDimStorage>> storageList;
  private final List<AbstractDimStorage> dirtyStorage;
  private CompoundTag saveTag;

  private DimStorageManager(Level level) {
    this.client = level.isClientSide();
    this.saveTag = new CompoundTag();

    storageMap = Collections.synchronizedMap(new HashMap<>());
    storageList = Collections.synchronizedMap(new HashMap<>());
    dirtyStorage = Collections.synchronizedList(new LinkedList<>());

    for (String key : PLUGINS.keySet()) {
      storageList.put(key, new ArrayList<>());
    }
  }

  public static void reloadManager(Level level) {
    if (level instanceof ServerLevel serverLevel) {
      SERVER_MANAGER = get(serverLevel);
    } else {
      CLIENT_MANAGER =  new DimStorageManager(level);
    }
  }

  public static DimStorageManager instance(Level level) {
    var client = level.isClientSide();
    DimStorageManager manager = client ? CLIENT_MANAGER : SERVER_MANAGER;
    if (manager == null) {
      reloadManager(level);
      manager = client ? CLIENT_MANAGER : SERVER_MANAGER;
    }
    return manager;
  }

  private static DimStorageManager get(ServerLevel level) {
    return level.getDataStorage()
        .computeIfAbsent(new SavedData.Factory<>(() -> new DimStorageManager(level), tag -> {
          var manager = new DimStorageManager(level);
          manager.load(tag);
          return manager;
        }), DATA_TAG);
  }

  public static void registerPlugin(DimStoragePlugin plugin) {
    PLUGINS.put(plugin.identifier(), plugin);

    if (SERVER_MANAGER != null) {
      SERVER_MANAGER.storageList.put(plugin.identifier(), new ArrayList<>());
    }
    if (CLIENT_MANAGER != null) {
      CLIENT_MANAGER.storageList.put(plugin.identifier(), new ArrayList<>());
    }
  }

  public boolean isServer() {
    return !client;
  }

  private void sendClientInfo(Player player) {
    for (var plugin : PLUGINS.entrySet()) {
      plugin.getValue().sendClientInfo(player, storageList.get(plugin.getKey()));
    }
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    for (var inv : dirtyStorage) {
      saveTag.put(buildKey(inv.freq, inv.type()), inv.saveToTag());
      inv.setClean();
    }
    dirtyStorage.clear();
    tag.put("inventory", saveTag);
    return tag;
  }

  private void load(CompoundTag tag) {
    this.saveTag = tag.getCompound("inventory");
  }

  private static String buildKey(Frequency frequency, String type) {
    return frequency + ",type=" + type;
  }

  public AbstractDimStorage getStorage(Frequency freq, String type) {
    String key = buildKey(freq, type);
    AbstractDimStorage storage = storageMap.get(key);

    if (storage == null) {
      storage = PLUGINS.get(type).createDimStorage(this, freq);

      if (!client && saveTag.contains(key)) {
        storage.loadFromTag(saveTag.getCompound(key));
      }

      storageMap.put(key, storage);
      storageList.get(type).add(storage);
    }
    return storage;
  }

  public void requestSave(AbstractDimStorage storage) {
    dirtyStorage.add(storage);
    this.setDirty();
  }

  public static class DimStorageSaveHandler {

    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) {
      if (event.getLevel() instanceof Level level) {
        reloadManager(level);
      } else {
        DimStorage.LOGGER.warn("Unable to reload the manager");
      }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
      instance(event.getEntity().level()).sendClientInfo(event.getEntity());
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
      instance(event.getEntity().level()).sendClientInfo(event.getEntity());
    }
  }
}
