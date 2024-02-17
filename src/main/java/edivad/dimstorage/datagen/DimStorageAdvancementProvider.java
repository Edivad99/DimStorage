package edivad.dimstorage.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import edivad.dimstorage.DimStorage;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.tools.Translations;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class DimStorageAdvancementProvider extends AdvancementProvider {

  public DimStorageAdvancementProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> registries,
      ExistingFileHelper existingFileHelper) {
    super(packOutput, registries, existingFileHelper, List.of(new Advancements()));
  }

  private static class Advancements implements AdvancementProvider.AdvancementGenerator {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer,
        ExistingFileHelper existingFileHelper) {
      var ROOT = Advancement.Builder.advancement()
          .display(Registration.DIMCORE.get(),
              Translations.ADVANCEMENTS_ROOT.translateTitle(),
              Translations.ADVANCEMENTS_ROOT.translateDescription(),
              new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
              AdvancementType.TASK,
              true,
              true,
              false)
          .addCriterion("inv_changed",
              InventoryChangeTrigger.TriggerInstance.hasItems(Registration.DIMCORE.get()))
          .save(consumer, DimStorage.rl("root"), existingFileHelper);

      var DIMCHEST = Advancement.Builder.advancement()
          .display(Registration.DIMCHEST_ITEM.get(),
              Translations.DIMCHEST_ADVANCEMENTS.translateTitle(),
              Translations.DIMCHEST_ADVANCEMENTS.translateDescription(),
              null,
              AdvancementType.TASK,
              true,
              true,
              false)
          .addCriterion("inv_changed",
              InventoryChangeTrigger.TriggerInstance.hasItems(Registration.DIMCHEST_ITEM.get()))
          .parent(ROOT)
          .save(consumer, DimStorage.rl("dimensional_chest"), existingFileHelper);

      Advancement.Builder.advancement()
          .display(Registration.DIMTANK_ITEM.get(),
              Translations.DIMTANK_ADVANCEMENTS.translateTitle(),
              Translations.DIMTANK_ADVANCEMENTS.translateDescription(),
              null,
              AdvancementType.TASK,
              true,
              true,
              false)
          .addCriterion("inv_changed",
              InventoryChangeTrigger.TriggerInstance.hasItems(Registration.DIMTANK_ITEM.get()))
          .parent(ROOT)
          .save(consumer, DimStorage.rl("dimensional_tank"), existingFileHelper);

      Advancement.Builder.advancement()
          .display(Registration.DIMTABLET.get(),
              Translations.DIMTABLET_ADVANCEMENTS.translateTitle(),
              Translations.DIMTABLET_ADVANCEMENTS.translateDescription(),
              null,
              AdvancementType.TASK,
              true,
              true,
              false)
          .addCriterion("inv_changed",
              InventoryChangeTrigger.TriggerInstance.hasItems(Registration.DIMTABLET.get()))
          .parent(DIMCHEST)
          .save(consumer, DimStorage.rl("dimensional_tablet"), existingFileHelper);
    }
  }
}
