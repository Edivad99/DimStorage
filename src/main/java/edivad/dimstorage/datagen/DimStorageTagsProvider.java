package edivad.dimstorage.datagen;

import java.util.concurrent.CompletableFuture;
import edivad.dimstorage.DimStorage;
import edivad.dimstorage.setup.ModRegistration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

public class DimStorageTagsProvider extends BlockTagsProvider {

  public DimStorageTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider) {
    super(packOutput, lookupProvider, DimStorage.ID);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .add(ModRegistration.DIMCHEST.get())
        .add(ModRegistration.DIMTANK.get());
    tag(BlockTags.NEEDS_IRON_TOOL)
        .add(ModRegistration.DIMCHEST.get())
        .add(ModRegistration.DIMTANK.get());
  }
}
