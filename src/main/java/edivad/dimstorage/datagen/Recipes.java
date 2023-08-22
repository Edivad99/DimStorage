package edivad.dimstorage.datagen;

import java.util.function.Consumer;
import edivad.dimstorage.setup.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

public class Recipes extends RecipeProvider {

  public Recipes(PackOutput packOutput) {
    super(packOutput);
  }

  @Override
  protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.DIMCORE.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.IRON_INGOT)
        .define('b', Items.REDSTONE)
        .define('c', Items.DIAMOND)
        .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.DIMWALL.get(), 4)
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.IRON_INGOT)
        .define('b', Items.REDSTONE)
        .define('c', Items.ENDER_PEARL)
        .unlockedBy(getHasName(Items.ENDER_PEARL), has(Items.ENDER_PEARL))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.SOLIDDIMCORE.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.IRON_INGOT)
        .define('b', Registration.DIMCORE.get())
        .unlockedBy(getHasName(Registration.DIMCORE.get()), has(Registration.DIMCORE.get()))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.DIMCHEST.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Registration.DIMWALL.get())
        .define('b', Registration.SOLIDDIMCORE.get())
        .unlockedBy(getHasName(Registration.SOLIDDIMCORE.get()),
            has(Registration.SOLIDDIMCORE.get()))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.DIMTABLET.get())
        .pattern("cdc")
        .pattern("cdc")
        .pattern("aba")
        .define('a', Items.OBSIDIAN)
        .define('b', Registration.SOLIDDIMCORE.get())
        .define('c', Items.IRON_INGOT)
        .define('d', Tags.Items.GLASS_PANES)
        .unlockedBy(getHasName(Registration.DIMCHEST.get()), has(Registration.DIMCHEST.get()))
        .save(consumer);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.DIMTANK.get())
        .pattern("ada")
        .pattern("dcd")
        .pattern("aba")
        .define('a', Registration.DIMWALL.get())
        .define('b', Registration.SOLIDDIMCORE.get())
        .define('c', Items.CAULDRON)
        .define('d', Tags.Items.GLASS)
        .unlockedBy(getHasName(Items.CAULDRON), has(Items.CAULDRON))
        .save(consumer);
  }
}
