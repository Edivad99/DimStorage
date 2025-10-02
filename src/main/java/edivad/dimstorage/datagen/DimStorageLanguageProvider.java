package edivad.dimstorage.datagen;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.setup.ModRegistration;
import edivad.dimstorage.tools.Translations;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class DimStorageLanguageProvider extends LanguageProvider {

  public DimStorageLanguageProvider(PackOutput packOutput) {
    super(packOutput, DimStorage.ID, "en_us");
  }

  @Override
  protected void addTranslations() {
    addItem(ModRegistration.DIMCORE, "Dimensional Core");
    addItem(ModRegistration.SOLIDDIMCORE, "Solid Dimensional Core");
    addItem(ModRegistration.DIMWALL, "Dimensional Wall");
    addItem(ModRegistration.DIMTABLET, "Dimensional Tablet");
    addBlock(ModRegistration.DIMCHEST, "Dimensional Chest");
    addBlock(ModRegistration.DIMTANK, "Dimensional Tank");

    add(Translations.ADVANCEMENTS_ROOT.title(), "Dimensional Core");
    add(Translations.ADVANCEMENTS_ROOT.desc(), "The power of dimensions");

    add(Translations.DIMCHEST_ADVANCEMENTS.title(), "Dimensional Chest");
    add(Translations.DIMCHEST_ADVANCEMENTS.desc(), "A more connected inventory");
    add(Translations.DIMTABLET_ADVANCEMENTS.title(), "Dimensional Tablet");
    add(Translations.DIMTABLET_ADVANCEMENTS.desc(), "A remote inventory");
    add(Translations.DIMTANK_ADVANCEMENTS.title(), "Dimensional Tank");
    add(Translations.DIMTANK_ADVANCEMENTS.desc(), "A multidimensional tank");

    add(Translations.LIQUID, "Liquid: %s");
    add(Translations.AMOUNT, "Amount: %s mb");
    add(Translations.TEMPERATURE, "Temperature: %s °C");
    add(Translations.LUMINOSITY, "Luminosity: %s");
    add(Translations.GAS, "Gaseous:");
    add(Translations.EMPTY, "Empty");
    add(Translations.YES, "Yes");
    add(Translations.NO, "No");
    add(Translations.EJECT, "Auto-eject");
    add(Translations.IDLE, "Idle");
    add(Translations.CHANGE, "Change");
    add(Translations.OWNER, "Owner:");
    add(Translations.FREQUENCY, "Frequency:");
    add(Translations.LOCKED, "Locked:");
    add(Translations.COLLECTING, "Collecting");

    add(Translations.PRESS, "Press");
    add(Translations.HOLD, "Hold");
    add(Translations.FOR_DETAILS, "for details");
    add(Translations.BIND_DIMCHEST, "right click to bind a DimChest");
    add(Translations.CHANGE_AUTO_COLLECT,
        "right click on a non DimChest block to enable/disable autocollect");

    add(Translations.JADE_DIMCHEST, "Enable custom visualization for DimChest");
    add(Translations.JADE_DIMTANK, "Enable custom visualization for DimTank");
  }
}
