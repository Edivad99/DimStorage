package edivad.dimstorage.tools;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Config {

	public static final String CATEGORY_GENERAL = "general";

	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

	public static ForgeConfigSpec COMMON_CONFIG;

	public static ForgeConfigSpec.BooleanValue DIMCHEST_ALLOWCONFIG;
	public static ForgeConfigSpec.BooleanValue DIMCHEST_ALLOWPRIVATENETWORK;
	public static ForgeConfigSpec.IntValue DIMCHEST_AREA;
	public static ForgeConfigSpec.ConfigValue<List<String>> DIMTABLET_LIST;

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

		DIMTABLET_LIST = COMMON_BUILDER
						.comment("A list of blocks that the DimTablet takes and transfers to the connected DimChest, [/dimstorage add] adds the item you have in the main hand to this list")
						.define("list", getListofBlock());

		//@formatter:on
		COMMON_BUILDER.pop();

		COMMON_CONFIG = COMMON_BUILDER.build();
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

	private static ArrayList<String> getListofBlock()
	{
		ArrayList<String> blocks = new ArrayList<>();

		blocks.add(getNamespace(Blocks.DIRT));
		blocks.add(getNamespace(Blocks.GRAVEL));
		blocks.add(getNamespace(Blocks.COBBLESTONE));
		blocks.add(getNamespace(Blocks.GRANITE));
		blocks.add(getNamespace(Blocks.DIORITE));
		blocks.add(getNamespace(Blocks.ANDESITE));
		blocks.add(getNamespace(Blocks.MOSSY_COBBLESTONE));
		blocks.add(getNamespace(Blocks.SAND));
		blocks.add(getNamespace(Blocks.SANDSTONE));
		blocks.add(getNamespace(Blocks.NETHERRACK));
		blocks.add(getNamespace(Blocks.END_STONE));
		return blocks;
	}

	private static String getNamespace(Block block)
	{
		return block.getRegistryName().toString();
	}
}
