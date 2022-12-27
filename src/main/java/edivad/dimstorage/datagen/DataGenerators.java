package edivad.dimstorage.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(event.includeServer(), new Recipes(packOutput));
        generator.addProvider(event.includeServer(), (DataProvider.Factory<LootTableProvider>) LootTables::create);
        generator.addProvider(event.includeServer(), new TagsProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new AdvancementProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeClient(), new Lang(packOutput));
    }
}
