package edivad.dimstorage.datagen;

import java.util.concurrent.CompletableFuture;
import edivad.dimstorage.DimStorage;
import edivad.dimstorage.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TagsProvider extends BlockTagsProvider {

  public TagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      ExistingFileHelper existingFileHelper) {
    super(packOutput, lookupProvider, DimStorage.ID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .add(Registration.DIMCHEST.get())
        .add(Registration.DIMTANK.get());
    tag(BlockTags.NEEDS_IRON_TOOL)
        .add(Registration.DIMCHEST.get())
        .add(Registration.DIMTANK.get());
  }
}
