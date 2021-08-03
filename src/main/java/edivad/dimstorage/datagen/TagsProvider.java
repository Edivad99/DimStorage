package edivad.dimstorage.datagen;

import edivad.dimstorage.Main;
import edivad.dimstorage.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class TagsProvider extends BlockTagsProvider {

    public TagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, Main.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(Registration.DIMCHEST.get());
        tag(BlockTags.NEEDS_IRON_TOOL).add(Registration.DIMCHEST.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(Registration.DIMTANK.get());
        tag(BlockTags.NEEDS_IRON_TOOL).add(Registration.DIMTANK.get());
    }
}
