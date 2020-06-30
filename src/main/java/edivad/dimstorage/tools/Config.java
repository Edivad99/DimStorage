package edivad.dimstorage.tools;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Config {

	public static final String CATEGORY_GENERAL = "general";

	public static ForgeConfigSpec SERVER_CONFIG;

	public static ForgeConfigSpec.BooleanValue DIMCHEST_ALLOWCONFIG;
	public static ForgeConfigSpec.BooleanValue DIMCHEST_ALLOWPRIVATENETWORK;
	public static ForgeConfigSpec.IntValue DIMCHEST_AREA;
	public static ForgeConfigSpec.ConfigValue<List<String>> DIMTABLET_LIST;

	static
	{
		ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
		
		SERVER_BUILDER.comment("General settings").push(CATEGORY_GENERAL);

		//@formatter:off
		DIMCHEST_ALLOWCONFIG = SERVER_BUILDER
						.comment("Allow players to change the DimChest's frequency, default: true")
						.define("allowFrequency", true);

		DIMCHEST_ALLOWPRIVATENETWORK = SERVER_BUILDER
						.comment("Allow players to make DimChest private, default: true")
						.define("allowPrivateNetwork", true);

		DIMCHEST_AREA = SERVER_BUILDER
						.comment("It is the diameter in which DimChest checks the inventories, default: 3")
						.defineInRange("diameter", 3, 3, 7);

		DIMTABLET_LIST = SERVER_BUILDER
						.comment("A list of blocks that the DimTablet takes and transfers to the connected DimChest, [/dimstorage add] adds the item you have in the main hand to this list")
						.define("list", getListofBlock());

		//@formatter:on
		SERVER_BUILDER.pop();

		SERVER_CONFIG = SERVER_BUILDER.build();
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
