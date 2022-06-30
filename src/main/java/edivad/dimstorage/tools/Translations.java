package edivad.dimstorage.tools;

import edivad.dimstorage.Main;
import edivad.edivadlib.tools.TranslationsAdvancement;

public class Translations {

    public static final TranslationsAdvancement ADVANCEMENTS_ROOT = new TranslationsAdvancement(Main.MODID, "root");
    public static final TranslationsAdvancement DIMCHEST_ADVANCEMENTS = new TranslationsAdvancement(Main.MODID, "dimensional_chest");
    public static final TranslationsAdvancement DIMTANK_ADVANCEMENTS = new TranslationsAdvancement(Main.MODID, "dimensional_tablet");
    public static final TranslationsAdvancement DIMTABLET_ADVANCEMENTS = new TranslationsAdvancement(Main.MODID, "dimensional_tank");

    public static final String LIQUID = "gui." + Main.MODID + ".liquid";
    public static final String AMOUNT = "gui." + Main.MODID + ".amount";
    public static final String TEMPERATURE = "gui." + Main.MODID + ".temperature";
    public static final String LUMINOSITY = "gui." + Main.MODID + ".luminosity";
    public static final String GAS = "gui." + Main.MODID + ".gas";
    public static final String EMPTY = "gui." + Main.MODID + ".empty";
    public static final String YES = "gui." + Main.MODID + ".yes";
    public static final String NO = "gui." + Main.MODID + ".no";
    public static final String EJECT = "gui." + Main.MODID + ".eject";
    public static final String IDLE = "gui." + Main.MODID + ".idle";
    public static final String CHANGE = "gui." + Main.MODID + ".change";
    public static final String OWNER = "gui." + Main.MODID + ".owner";
    public static final String FREQUENCY = "gui." + Main.MODID + ".frequency";
    public static final String LOCKED = "gui." + Main.MODID + ".locked";
    public static final String COLLECTING = "gui." + Main.MODID + ".collecting";
    public static final String PRESS = "message." + Main.MODID + ".press";
    public static final String HOLD = "message." + Main.MODID + ".hold";
    public static final String FOR_DETAILS = "message." + Main.MODID + ".for_details";
    public static final String BIND_DIMCHEST = "message." + Main.MODID + ".bind_dimchest";
    public static final String CHANGE_AUTO_COLLECT = "message." + Main.MODID + ".changeAutoCollect";


    public static final String JADE_DIMCHEST = "config.jade.plugin_" + Main.MODID + ".dim_block_base";
    public static final String JADE_DIMTANK = "config.jade.plugin_" + Main.MODID + ".dim_tank";
}
