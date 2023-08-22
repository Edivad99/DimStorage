package edivad.dimstorage.tools;

import edivad.dimstorage.DimStorage;
import edivad.edivadlib.tools.TranslationsAdvancement;

public class Translations {

  public static final TranslationsAdvancement ADVANCEMENTS_ROOT =
      new TranslationsAdvancement(DimStorage.ID, "root");
  public static final TranslationsAdvancement DIMCHEST_ADVANCEMENTS =
      new TranslationsAdvancement(DimStorage.ID, "dimensional_chest");
  public static final TranslationsAdvancement DIMTANK_ADVANCEMENTS =
      new TranslationsAdvancement(DimStorage.ID, "dimensional_tablet");
  public static final TranslationsAdvancement DIMTABLET_ADVANCEMENTS =
      new TranslationsAdvancement(DimStorage.ID, "dimensional_tank");

  public static final String LIQUID = "gui." + DimStorage.ID + ".liquid";
  public static final String AMOUNT = "gui." + DimStorage.ID + ".amount";
  public static final String TEMPERATURE = "gui." + DimStorage.ID + ".temperature";
  public static final String LUMINOSITY = "gui." + DimStorage.ID + ".luminosity";
  public static final String GAS = "gui." + DimStorage.ID + ".gas";
  public static final String EMPTY = "gui." + DimStorage.ID + ".empty";
  public static final String YES = "gui." + DimStorage.ID + ".yes";
  public static final String NO = "gui." + DimStorage.ID + ".no";
  public static final String EJECT = "gui." + DimStorage.ID + ".eject";
  public static final String IDLE = "gui." + DimStorage.ID + ".idle";
  public static final String CHANGE = "gui." + DimStorage.ID + ".change";
  public static final String OWNER = "gui." + DimStorage.ID + ".owner";
  public static final String FREQUENCY = "gui." + DimStorage.ID + ".frequency";
  public static final String LOCKED = "gui." + DimStorage.ID + ".locked";
  public static final String COLLECTING = "gui." + DimStorage.ID + ".collecting";
  public static final String PRESS = "message." + DimStorage.ID + ".press";
  public static final String HOLD = "message." + DimStorage.ID + ".hold";
  public static final String FOR_DETAILS = "message." + DimStorage.ID + ".for_details";
  public static final String BIND_DIMCHEST = "message." + DimStorage.ID + ".bind_dimchest";
  public static final String CHANGE_AUTO_COLLECT =
      "message." + DimStorage.ID + ".changeAutoCollect";


  public static final String JADE_DIMCHEST =
      "config.jade.plugin_" + DimStorage.ID + ".dim_block_base";
  public static final String JADE_DIMTANK = "config.jade.plugin_" + DimStorage.ID + ".dim_tank";
}
