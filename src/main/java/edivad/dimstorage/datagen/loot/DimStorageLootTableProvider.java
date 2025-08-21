package edivad.dimstorage.datagen.loot;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class DimStorageLootTableProvider extends LootTableProvider {

  public DimStorageLootTableProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
    super(packOutput, Set.of(), List.of(
        new SubProviderEntry(DimStorageBlockLoot::new, LootContextParamSets.BLOCK)
    ), registries);
  }
}
