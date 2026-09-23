package edivad.dimstorage.datagen;

import edivad.dimstorage.setup.ModRegistration;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import net.neoforged.neoforge.common.Tags;

public class DimStorageRecipeProvider extends RecipeProvider {

  public DimStorageRecipeProvider(BootstrapContext<Recipe<?>> recipeOutput,
          BootstrapContext<Advancement> advancementOutput) {
    super(recipeOutput, advancementOutput);
  }

  public static MultiRegistryBootstrap create() {
    return RecipeProvider.asBootstrap(DimStorageRecipeProvider::new);
  }

  @Override
  protected void buildRecipes() {
    ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, ModRegistration.DIMCORE.get())
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.IRON_INGOT)
        .define('b', Items.REDSTONE)
        .define('c', Items.DIAMOND)
        .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
        .save(output);

    ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, ModRegistration.DIMWALL.get(), 4)
        .pattern("aba")
        .pattern("bcb")
        .pattern("aba")
        .define('a', Items.IRON_INGOT)
        .define('b', Items.REDSTONE)
        .define('c', Items.ENDER_PEARL)
        .unlockedBy(getHasName(Items.ENDER_PEARL), has(Items.ENDER_PEARL))
        .save(output);

    ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, ModRegistration.SOLIDDIMCORE.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', Items.IRON_INGOT)
        .define('b', ModRegistration.DIMCORE.get())
        .unlockedBy(getHasName(ModRegistration.DIMCORE.get()), has(ModRegistration.DIMCORE.get()))
        .save(output);

    ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, ModRegistration.DIMCHEST.get())
        .pattern("aaa")
        .pattern("aba")
        .pattern("aaa")
        .define('a', ModRegistration.DIMWALL.get())
        .define('b', ModRegistration.SOLIDDIMCORE.get())
        .unlockedBy(getHasName(ModRegistration.SOLIDDIMCORE.get()),
            has(ModRegistration.SOLIDDIMCORE.get()))
        .save(output);

    ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, ModRegistration.DIMTABLET.get())
        .pattern("cdc")
        .pattern("cdc")
        .pattern("aba")
        .define('a', Items.OBSIDIAN)
        .define('b', ModRegistration.SOLIDDIMCORE.get())
        .define('c', Items.IRON_INGOT)
        .define('d', Tags.Items.GLASS_PANES)
        .unlockedBy(getHasName(ModRegistration.DIMCHEST.get()), has(ModRegistration.DIMCHEST.get()))
        .save(output);

    ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, ModRegistration.DIMTANK.get())
        .pattern("ada")
        .pattern("dcd")
        .pattern("aba")
        .define('a', ModRegistration.DIMWALL.get())
        .define('b', ModRegistration.SOLIDDIMCORE.get())
        .define('c', Items.CAULDRON)
        .define('d', Tags.Items.GLASS_BLOCKS)
        .unlockedBy(getHasName(Items.CAULDRON), has(Items.CAULDRON))
        .save(output);
  }
}
