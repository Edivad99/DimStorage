package edivad.dimstorage.datagen;

import java.util.List;
import edivad.dimstorage.DimStorage;
import edivad.dimstorage.setup.ModRegistration;
import edivad.dimstorage.tools.Translations;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.registries.SingleRegistryBootstrap;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;

public class DimStorageAdvancementProvider {

  public static SingleRegistryBootstrap<Advancement> create() {
    return new AdvancementProvider(List.of(Advancements::new));
  }

  private static class Advancements extends AdvancementSubProvider {

    private Advancements(BootstrapContext<Advancement> output) {
      super(output);
    }

    @Override
    public void generate() {
      var ROOT = Advancement.Builder.advancement()
          .rootDisplay(ModRegistration.DIMCORE.get(),
              Translations.ADVANCEMENTS_ROOT.translateTitle(),
              Translations.ADVANCEMENTS_ROOT.translateDescription(),
              Identifier.withDefaultNamespace("textures/gui/advancements/backgrounds/stone.png"),
              AdvancementType.TASK,
              true,
              true,
              false)
          .addCriterion("inv_changed",
              InventoryChangeTrigger.TriggerInstance.hasItems(ModRegistration.DIMCORE.get()))
          .save(output, DimStorage.id("root").toString());

      var DIMCHEST = Advancement.Builder.advancement()
          .display(ModRegistration.DIMCHEST_ITEM.get(),
              Translations.DIMCHEST_ADVANCEMENTS.translateTitle(),
              Translations.DIMCHEST_ADVANCEMENTS.translateDescription(),
              AdvancementType.TASK,
              true,
              true,
              false)
          .addCriterion("inv_changed",
              InventoryChangeTrigger.TriggerInstance.hasItems(ModRegistration.DIMCHEST_ITEM.get()))
          .parent(ROOT)
          .save(output, DimStorage.id("dimensional_chest").toString());

      Advancement.Builder.advancement()
          .display(ModRegistration.DIMTANK_ITEM.get(),
              Translations.DIMTANK_ADVANCEMENTS.translateTitle(),
              Translations.DIMTANK_ADVANCEMENTS.translateDescription(),
              AdvancementType.TASK,
              true,
              true,
              false)
          .addCriterion("inv_changed",
              InventoryChangeTrigger.TriggerInstance.hasItems(ModRegistration.DIMTANK_ITEM.get()))
          .parent(ROOT)
          .save(output, DimStorage.id("dimensional_tank").toString());

      Advancement.Builder.advancement()
          .display(ModRegistration.DIMTABLET.get(),
              Translations.DIMTABLET_ADVANCEMENTS.translateTitle(),
              Translations.DIMTABLET_ADVANCEMENTS.translateDescription(),
              AdvancementType.TASK,
              true,
              true,
              false)
          .addCriterion("inv_changed",
              InventoryChangeTrigger.TriggerInstance.hasItems(ModRegistration.DIMTABLET.get()))
          .parent(DIMCHEST)
          .save(output, DimStorage.id("dimensional_tablet").toString());
    }
  }
}
