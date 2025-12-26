package edivad.dimstorage.datagen.models;

import java.util.Optional;
import edivad.dimstorage.DimStorage;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;

public class DimStorageModelTemplates {

  private static final ModelTemplate DIM_TANK_TEMPLATE = new ModelTemplate(
      Optional.of(ModelLocationUtils
          .decorateBlockModelLocation(DimStorage.id("dimensional_tank_template").toString())),
      Optional.empty(),
      TextureSlot.DOWN,
      TextureSlot.SIDE,
      TextureSlot.PARTICLE
  );

  static final TexturedModel.Provider DIM_TANK_TEMPLATE_PROVIDER = TexturedModel.createDefault(
      block -> {
        var bottom = TextureMapping.getBlockTexture(block, "_bottom");
        var side = TextureMapping.getBlockTexture(block, "_side");
        return new TextureMapping()
            .put(TextureSlot.SIDE, side)
            .put(TextureSlot.DOWN, bottom)
            .put(TextureSlot.PARTICLE, side);
      },
      DIM_TANK_TEMPLATE
  );
}
