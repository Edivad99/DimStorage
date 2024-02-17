package edivad.dimstorage.datagen;

import java.util.List;
import java.util.Set;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class DimStorageLootTableProvider extends LootTableProvider {

  public DimStorageLootTableProvider(PackOutput packOutput) {
    super(packOutput, Set.of(), List.of(
        new SubProviderEntry(DimStorageBlockLoot::new, LootContextParamSets.BLOCK)
    ));
  }
}
