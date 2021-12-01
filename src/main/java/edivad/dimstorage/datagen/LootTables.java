package edivad.dimstorage.datagen;

import edivad.dimstorage.setup.Registration;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableProvider {

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        lootTables.put(Registration.DIMCHEST.get(), createStandardTable(Registration.DIMCHEST.get(), Registration.DIMCHEST_TILE.get()));
        lootTables.put(Registration.DIMTANK.get(), createStandardTable(Registration.DIMTANK.get(), Registration.DIMTANK_TILE.get()));
    }
}
