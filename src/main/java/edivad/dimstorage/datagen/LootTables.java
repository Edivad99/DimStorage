package edivad.dimstorage.datagen;

import edivad.dimstorage.setup.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;

public class LootTables extends BlockLootSubProvider {

    public LootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.add(Registration.DIMCHEST.get(), (block -> createStandardTable(block, Registration.DIMCHEST_TILE.get())));
        this.add(Registration.DIMTANK.get(), (block -> createStandardTable(block, Registration.DIMTANK_TILE.get())));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of(Registration.DIMCHEST.get(), Registration.DIMTANK.get());
    }

    private LootTable.Builder createStandardTable(Block block, BlockEntityType blockEntityType) {
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
        LootPool.Builder builder = LootPool.lootPool()
                .name(name)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block)
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                        .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("Frequency", "DimStorage.Frequency", CopyNbtFunction.MergeStrategy.REPLACE))
                        .apply(SetContainerContents.setContents(blockEntityType)
                                .withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("minecraft", "contents"))))
                );
        return LootTable.lootTable().withPool(builder);
    }

    public static LootTableProvider create(PackOutput packOutput) {
        return new LootTableProvider(packOutput, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(LootTables::new, LootContextParamSets.BLOCK)
        ));
    }
}
