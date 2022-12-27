package edivad.dimstorage.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import edivad.dimstorage.Main;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.tools.Translations;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

public class AdvancementProvider extends ForgeAdvancementProvider {

    public AdvancementProvider(PackOutput packOutput,
        CompletableFuture<HolderLookup.Provider> registries,
        ExistingFileHelper existingFileHelper) {
        super(packOutput, registries, existingFileHelper, List.of(new Advancements()));
    }

    private static class Advancements implements ForgeAdvancementProvider.AdvancementGenerator {

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<Advancement> consumer,
            ExistingFileHelper existingFileHelper) {
            var ROOT_DIMCORE = Advancement.Builder.advancement()
                .display(Registration.DIMCORE.get(),
                    Translations.ADVANCEMENTS_ROOT.translateTitle(),
                    Translations.ADVANCEMENTS_ROOT.translateDescription(),
                    new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
                    FrameType.TASK,
                    true,
                    true,
                    false)
                .addCriterion("inv_changed", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.DIMCORE.get()))
                .save(consumer, new ResourceLocation(Main.MODID, "root"), existingFileHelper);

            var DIMCHEST = Advancement.Builder.advancement()
                .display(Registration.DIMCHEST_ITEM.get(),
                    Translations.DIMCHEST_ADVANCEMENTS.translateTitle(),
                    Translations.DIMCHEST_ADVANCEMENTS.translateDescription(),
                    null,
                    FrameType.TASK,
                    true,
                    true,
                    false)
                .addCriterion("inv_changed", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.DIMCHEST_ITEM.get()))
                .parent(ROOT_DIMCORE)
                .save(consumer, new ResourceLocation(Main.MODID, "dimensional_chest"), existingFileHelper);

            Advancement.Builder.advancement()
                .display(Registration.DIMTANK_ITEM.get(),
                    Translations.DIMTANK_ADVANCEMENTS.translateTitle(),
                    Translations.DIMTANK_ADVANCEMENTS.translateDescription(),
                    null,
                    FrameType.TASK,
                    true,
                    true,
                    false)
                .addCriterion("inv_changed", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.DIMTANK_ITEM.get()))
                .parent(ROOT_DIMCORE)
                .save(consumer, new ResourceLocation(Main.MODID, "dimensional_tank"), existingFileHelper);

            Advancement.Builder.advancement()
                .display(Registration.DIMTABLET.get(),
                    Translations.DIMTABLET_ADVANCEMENTS.translateTitle(),
                    Translations.DIMTABLET_ADVANCEMENTS.translateDescription(),
                    null,
                    FrameType.TASK,
                    true,
                    true,
                    false)
                .addCriterion("inv_changed", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.DIMTABLET.get()))
                .parent(DIMCHEST)
                .save(consumer, new ResourceLocation(Main.MODID, "dimensional_tablet"), existingFileHelper);
        }
    }
}
