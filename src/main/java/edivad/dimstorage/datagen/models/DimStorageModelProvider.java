package edivad.dimstorage.datagen.models;

import java.util.stream.Stream;
import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blocks.DimChestBlock;
import edivad.dimstorage.blocks.DimTankBlock;
import edivad.dimstorage.setup.Registration;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;

public class DimStorageModelProvider extends ModelProvider {

  public DimStorageModelProvider(PackOutput output) {
    super(output, DimStorage.ID);
  }

  @Override
  protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
    itemModels.generateFlatItem(Registration.DIMWALL.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(Registration.DIMCORE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(Registration.SOLIDDIMCORE.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(Registration.DIMTABLET.get(), ModelTemplates.FLAT_ITEM);

    createDimChest(blockModels, Registration.DIMCHEST.get());
    createDimTank(blockModels, Registration.DIMTANK.get());
  }

  private void createDimChest(BlockModelGenerators blockModels, DimChestBlock block) {
    var tm = new TextureMapping()
        .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"))
        .put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"))
        .put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_sides"))
        .put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_sides"))
        .put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_sides"))
        .put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_sides"))
        .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_top"));

    var model = ModelTemplates.CUBE
        .extend().transform(ItemDisplayContext.GUI, transformVecBuilder -> {
          transformVecBuilder.rotation(25, 45, 0F)
              .translation(0F, 0F, 0F)
              .scale(0.625F);
        }).build()
        .create(block, tm, blockModels.modelOutput);

    blockModels.blockStateOutput.accept(BlockModelGenerators
        .createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
    blockModels.registerSimpleItemModel(block, model);
  }

  private void createDimTank(BlockModelGenerators blockModels, DimTankBlock block) {
    var model = DimStorageModelTemplates.DIM_TANK_TEMPLATE_PROVIDER
        .updateTemplate(modelTemplate -> modelTemplate.extend().renderType("cutout").build())
        .create(block, blockModels.modelOutput);

    blockModels.blockStateOutput.accept(BlockModelGenerators
        .createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
    blockModels.registerSimpleItemModel(block, model);
  }

  @Override
  protected Stream<? extends Holder<Block>> getKnownBlocks() {
    return Stream.of();
  }

  @Override
  protected Stream<? extends Holder<Item>> getKnownItems() {
    return Stream.of();
  }
}
