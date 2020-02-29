package edivad.dimstorage.tools;

import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Config {

	public static final String CATEGORY_GENERAL = "general";

	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
	//private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

	public static ForgeConfigSpec COMMON_CONFIG;
	//public static ForgeConfigSpec CLIENT_CONFIG;

	public static ForgeConfigSpec.BooleanValue DIMCHEST_ALLOWCONFIG;
	public static ForgeConfigSpec.BooleanValue DIMCHEST_ALLOWPRIVATENETWORK;
	public static ForgeConfigSpec.IntValue DIMCHEST_AREA;
	public static ForgeConfigSpec.ConfigValue<String>PATTERN_DIMTABLET;

	static
	{
		COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);

		//@formatter:off
		DIMCHEST_ALLOWCONFIG = COMMON_BUILDER
						.comment("Allow players to change the DimChest's frequency, default: true")
						.define("allowFrequency", true);
		
		DIMCHEST_ALLOWPRIVATENETWORK = COMMON_BUILDER
						.comment("Allow players to make DimChest private, default: true")
						.define("allowPrivateNetwork", true);
		
		DIMCHEST_AREA = COMMON_BUILDER
						.comment("It is the diameter in which DimChest checks the inventories, default: 3")
						.defineInRange("diameter", 3, 3, 7);
		
		PATTERN_DIMTABLET = COMMON_BUILDER
						.comment("Regular expression to take the items in the inventory and put them in the DimTablet")
						.define("path", "^.*_ore.*|diamond|redstone|emerald|cobblestone|coal|lapis_lazuli");
		//@formatter:on
		COMMON_BUILDER.pop();

		COMMON_CONFIG = COMMON_BUILDER.build();
		//CLIENT_CONFIG = CLIENT_BUILDER.build();
	}

	public static void loadConfig(ForgeConfigSpec spec, Path path)
	{
		//@formatter:off
		final CommentedFileConfig configData = CommentedFileConfig
															.builder(path)
															.sync()
															.autosave()
															.writingMode(WritingMode.REPLACE)
															.build();
		//@formatter:on
		configData.load();
		spec.setConfig(configData);
	}
}
