package edivad.dimstorage.datagen;

import java.util.List;
import java.util.Set;
import edivad.dimstorage.setup.Registration;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class DimStorageBlockLoot extends BlockLootSubProvider {

  public DimStorageBlockLoot() {
    super(Set.of(), FeatureFlags.REGISTRY.allFlags());
  }

  @Override
  protected void generate() {
    this.add(Registration.DIMCHEST.get(), DimStorageBlockLoot::createStandardTable);
    this.add(Registration.DIMTANK.get(), DimStorageBlockLoot::createStandardTable);
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return List.of(Registration.DIMCHEST.get(), Registration.DIMTANK.get());
  }

  private static LootTable.Builder createStandardTable(Block block) {
    var builder = LootPool.lootPool()
        .setRolls(ConstantValue.exactly(1))
        .add(LootItem.lootTableItem(block)
            .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
            .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy("Frequency", "DimStorage.Frequency", CopyNbtFunction.MergeStrategy.REPLACE))
        ).when(ExplosionCondition.survivesExplosion());
    return LootTable.lootTable().withPool(builder);
  }
}
