package edivad.dimstorage.datagen;

import java.util.function.Consumer;

import edivad.dimstorage.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        //@formatter:off
		ShapedRecipeBuilder.shapedRecipe(Registration.DIMCORE.get())
			.patternLine("aba")
	        .patternLine("bcb")
	        .patternLine("aba")
	        .key('a', Items.IRON_INGOT)
	        .key('b', Items.REDSTONE)
	        .key('c', Items.DIAMOND)
	        //.setGroup(Main.MODID)
	        .addCriterion("diamond", hasItem(Items.DIAMOND))
	        .build(consumer);

		ShapedRecipeBuilder.shapedRecipe(Registration.DIMWALL.get(), 4)
	        .patternLine("aba")
	        .patternLine("bcb")
	        .patternLine("aba")
	        .key('a', Items.IRON_INGOT)
	        .key('b', Items.REDSTONE)
	        .key('c', Items.ENDER_PEARL)
	        //.setGroup(Main.MODID)
	        .addCriterion("ender_pearl", hasItem(Items.ENDER_PEARL))
	        .build(consumer);

		ShapedRecipeBuilder.shapedRecipe(Registration.SOLIDDIMCORE.get())
	        .patternLine("aaa")
	        .patternLine("aba")
	        .patternLine("aaa")
	        .key('a', Items.IRON_INGOT)
	        .key('b', Registration.DIMCORE.get())
	        //.setGroup(Main.MODID)
	        .addCriterion(Registration.DIMCORE.getId().getPath(), hasItem(Registration.DIMCORE.get()))
	        .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.DIMCHEST.get())
	        .patternLine("aaa")
	        .patternLine("aba")
	        .patternLine("aaa")
	        .key('a', Registration.DIMWALL.get())
	        .key('b', Registration.SOLIDDIMCORE.get())
	        //.setGroup(Main.MODID)
	        .addCriterion(Registration.SOLIDDIMCORE.getId().getPath(), hasItem(Registration.SOLIDDIMCORE.get()))
	        .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.DIMTABLET.get())
	        .patternLine("cdc")
	        .patternLine("cdc")
	        .patternLine("aba")
	        .key('a', Items.OBSIDIAN)
	        .key('b', Registration.SOLIDDIMCORE.get())
	        .key('c', Items.IRON_INGOT)
	        .key('d', Tags.Items.GLASS_PANES)
	        //.setGroup(Main.MODID)
	        .addCriterion(Registration.DIMCHEST.getId().getPath(), hasItem(Registration.DIMCHEST.get()))
	        .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.DIMTANK.get())
	        .patternLine("ada")
	        .patternLine("dcd")
	        .patternLine("aba")
	        .key('a', Registration.DIMWALL.get())
	        .key('b', Registration.SOLIDDIMCORE.get())
	        .key('c', Items.CAULDRON)
	        .key('d', Tags.Items.GLASS)
	        //.setGroup(Main.MODID)
	        .addCriterion("cauldron", hasItem(Items.CAULDRON))
	        .build(consumer);
        //@formatter:on
    }
}
