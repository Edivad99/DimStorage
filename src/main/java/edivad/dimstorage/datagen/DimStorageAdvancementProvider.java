package edivad.dimstorage.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import edivad.dimstorage.DimStorage;
import edivad.dimstorage.setup.ModRegistration;
import edivad.dimstorage.tools.Translations;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.resources.ResourceLocation;

public class DimStorageAdvancementProvider extends AdvancementProvider {

  public DimStorageAdvancementProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> registries) {
    super(packOutput, registries, List.of(new Advancements()));
  }

  private static class Advancements implements AdvancementSubProvider {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> writer) {
      var ROOT = Advancement.Builder.advancement()
          .display(ModRegistration.DIMCORE.get(),
              Translations.ADVANCEMENTS_ROOT.translateTitle(),
              Translations.ADVANCEMENTS_ROOT.translateDescription(),
              ResourceLocation.withDefaultNamespace("textures/gui/advancements/backgrounds/stone.png"),
              AdvancementType.TASK,
              true,
              true,
              false)
          .addCriterion("inv_changed",
              InventoryChangeTrigger.TriggerInstance.hasItems(ModRegistration.DIMCORE.get()))
          .save(writer, DimStorage.rl("root"));

      var DIMCHEST = Advancement.Builder.advancement()
          .display(ModRegistration.DIMCHEST_ITEM.get(),
              Translations.DIMCHEST_ADVANCEMENTS.translateTitle(),
              Translations.DIMCHEST_ADVANCEMENTS.translateDescription(),
              null,
              AdvancementType.TASK,
              true,
              true,
              false)
          .addCriterion("inv_changed",
              InventoryChangeTrigger.TriggerInstance.hasItems(ModRegistration.DIMCHEST_ITEM.get()))
          .parent(ROOT)
          .save(writer, DimStorage.rl("dimensional_chest"));

      Advancement.Builder.advancement()
          .display(ModRegistration.DIMTANK_ITEM.get(),
              Translations.DIMTANK_ADVANCEMENTS.translateTitle(),
              Translations.DIMTANK_ADVANCEMENTS.translateDescription(),
              null,
              AdvancementType.TASK,
              true,
              true,
              false)
          .addCriterion("inv_changed",
              InventoryChangeTrigger.TriggerInstance.hasItems(ModRegistration.DIMTANK_ITEM.get()))
          .parent(ROOT)
          .save(writer, DimStorage.rl("dimensional_tank"));

      Advancement.Builder.advancement()
          .display(ModRegistration.DIMTABLET.get(),
              Translations.DIMTABLET_ADVANCEMENTS.translateTitle(),
              Translations.DIMTABLET_ADVANCEMENTS.translateDescription(),
              null,
              AdvancementType.TASK,
              true,
              true,
              false)
          .addCriterion("inv_changed",
              InventoryChangeTrigger.TriggerInstance.hasItems(ModRegistration.DIMTABLET.get()))
          .parent(DIMCHEST)
          .save(writer, DimStorage.rl("dimensional_tablet"));
    }
  }
}
