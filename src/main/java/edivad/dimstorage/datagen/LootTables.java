package edivad.dimstorage.datagen;

import edivad.dimstorage.Main;
import edivad.dimstorage.setup.Registration;
import edivad.edivadlib.datagen.BaseLootTableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;

public class LootTables extends BaseLootTableProvider {

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn, Main.MODNAME);
    }

    @Override
    protected void addTables() {
        lootTables.put(Registration.DIMCHEST.get(), createStandardTable(Registration.DIMCHEST.get(), Registration.DIMCHEST_TILE.get()));
        lootTables.put(Registration.DIMTANK.get(), createStandardTable(Registration.DIMTANK.get(), Registration.DIMTANK_TILE.get()));
    }

    private LootTable.Builder createStandardTable(Block block, BlockEntityType blockEntityType) {
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
        LootPool.Builder builder = LootPool.lootPool()//
                .name(name)//
                .setRolls(ConstantValue.exactly(1))//
                .add(LootItem.lootTableItem(block)//
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))//
                        .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)//
                                .copy("Frequency", "DimStorage.Frequency", CopyNbtFunction.MergeStrategy.REPLACE))//
                        .apply(SetContainerContents.setContents(blockEntityType)//
                                .withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("minecraft", "contents"))))//
                );
        return LootTable.lootTable().withPool(builder);
    }

}
