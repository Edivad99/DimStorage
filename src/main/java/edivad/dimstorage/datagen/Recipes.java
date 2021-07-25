package edivad.dimstorage.datagen;

import java.util.function.Consumer;

import edivad.dimstorage.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
    {

        ShapedRecipeBuilder.shaped(Registration.DIMCORE.get())//
                .pattern("aba")//
                .pattern("bcb")//
                .pattern("aba")//
                .define('a', Items.IRON_INGOT)//
                .define('b', Items.REDSTONE)//
                .define('c', Items.DIAMOND)//
                //.setGroup(Main.MODID)//
                .unlockedBy("diamond", has(Items.DIAMOND))//
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.DIMWALL.get(), 4)//
                .pattern("aba")//
                .pattern("bcb")//
                .pattern("aba")//
                .define('a', Items.IRON_INGOT)//
                .define('b', Items.REDSTONE)//
                .define('c', Items.ENDER_PEARL)//
                //.setGroup(Main.MODID)//
                .unlockedBy("ender_pearl", has(Items.ENDER_PEARL))//
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.SOLIDDIMCORE.get())//
                .pattern("aaa")//
                .pattern("aba")//
                .pattern("aaa")//
                .define('a', Items.IRON_INGOT)//
                .define('b', Registration.DIMCORE.get())//
                //.setGroup(Main.MODID)//
                .unlockedBy(Registration.DIMCORE.getId().getPath(), has(Registration.DIMCORE.get()))//
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.DIMCHEST.get())//
                .pattern("aaa")//
                .pattern("aba")//
                .pattern("aaa")//
                .define('a', Registration.DIMWALL.get())//
                .define('b', Registration.SOLIDDIMCORE.get())//
                //.setGroup(Main.MODID)//
                .unlockedBy(Registration.SOLIDDIMCORE.getId().getPath(), has(Registration.SOLIDDIMCORE.get()))//
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.DIMTABLET.get())//
                .pattern("cdc")//
                .pattern("cdc")//
                .pattern("aba")//
                .define('a', Items.OBSIDIAN)//
                .define('b', Registration.SOLIDDIMCORE.get())//
                .define('c', Items.IRON_INGOT)//
                .define('d', Tags.Items.GLASS_PANES)//
                //.setGroup(Main.MODID)//
                .unlockedBy(Registration.DIMCHEST.getId().getPath(), has(Registration.DIMCHEST.get()))//
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.DIMTANK.get())//
                .pattern("ada")//
                .pattern("dcd")//
                .pattern("aba")//
                .define('a', Registration.DIMWALL.get())//
                .define('b', Registration.SOLIDDIMCORE.get())//
                .define('c', Items.CAULDRON)//
                .define('d', Tags.Items.GLASS)//
                //.setGroup(Main.MODID)//
                .unlockedBy("cauldron", has(Items.CAULDRON))//
                .save(consumer);
    }
}
