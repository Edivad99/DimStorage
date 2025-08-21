package edivad.dimstorage.client.render.blockentity;

import org.joml.Matrix4f;
import com.mojang.blaze3d.vertex.PoseStack;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.storage.DimTankStorage;
import edivad.edivadlib.tools.utils.FluidUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;

public class DimTankRenderer implements BlockEntityRenderer<BlockEntityDimTank> {

  private static final float TANK_THICKNESS = 0.1f;

  public DimTankRenderer(BlockEntityRendererProvider.Context context) {
  }

  @Override
  public void render(BlockEntityDimTank blockentity, float partialTick, PoseStack poseStack,
      MultiBufferSource bufferSource, int packedLight, int packedOverlay, Vec3 cameraPos) {
    if (blockentity.isRemoved() || blockentity.liquidState.clientLiquid == null) {
      return;
    }

    poseStack.pushPose();
    renderFluid(blockentity, poseStack, bufferSource);
    poseStack.popPose();
  }

  private void renderFluid(BlockEntityDimTank blockentity, PoseStack poseStack,
      MultiBufferSource bufferIn) {
    var fluid = blockentity.liquidState.clientLiquid;
    float scale = (1.0f - TANK_THICKNESS / 2 - TANK_THICKNESS) * fluid.getAmount()
        / (DimTankStorage.CAPACITY);
    if (scale > 0.0f) {
      Matrix4f matrix4f = poseStack.last().pose();
      TextureAtlasSprite sprite = FluidUtils.getFluidTexture(fluid);
      if (sprite == null) {
        return;
      }
      var renderer = bufferIn.getBuffer(RenderType.text(sprite.atlasLocation()));

      float u1 = sprite.getU0();
      float v1 = sprite.getV0();
      float u2 = sprite.getU1();
      float v2 = sprite.getV1();

      float margin = 0.9f;
      float offset = 0.1f;

      final int color = FluidUtils.getLiquidColorWithBiome(fluid, blockentity);
      final float r = FluidUtils.getRed(color);
      final float g = FluidUtils.getGreen(color);
      final float b = FluidUtils.getBlue(color);
      final float a = FluidUtils.getAlpha(color);
      final int light = 15728880;

      // Top
      renderer.addVertex(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS,
          TANK_THICKNESS + offset).setColor(r, g, b, a).setUv(u1, v1).setLight(light);
      renderer.addVertex(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS,
          margin - TANK_THICKNESS).setColor(r, g, b, a).setUv(u1, v2).setLight(light);
      renderer.addVertex(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS,
          margin - TANK_THICKNESS).setColor(r, g, b, a).setUv(u2, v2).setLight(light);
      renderer.addVertex(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS,
          TANK_THICKNESS + offset).setColor(r, g, b, a).setUv(u2, v1).setLight(light);

      // Bottom
      renderer.addVertex(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset)
          .setColor(r, g, b, a).setUv(u2, v1).setLight(light);
      renderer.addVertex(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS)
          .setColor(r, g, b, a).setUv(u2, v2).setLight(light);
      renderer.addVertex(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS)
          .setColor(r, g, b, a).setUv(u1, v2).setLight(light);
      renderer.addVertex(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset)
          .setColor(r, g, b, a).setUv(u1, v1).setLight(light);

      // Sides
      //NORTH
      renderer.addVertex(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS,
          margin - TANK_THICKNESS).setColor(r, g, b, a).setUv(u1, v1).setLight(light);
      renderer.addVertex(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS)
          .setColor(r, g, b, a).setUv(u1, v2).setLight(light);
      renderer.addVertex(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS)
          .setColor(r, g, b, a).setUv(u2, v2).setLight(light);
      renderer.addVertex(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS,
          margin - TANK_THICKNESS).setColor(r, g, b, a).setUv(u2, v1).setLight(light);

      //SOUTH
      renderer.addVertex(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS,
          TANK_THICKNESS + offset).setColor(r, g, b, a).setUv(u2, v1).setLight(light);
      renderer.addVertex(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset)
          .setColor(r, g, b, a).setUv(u2, v2).setLight(light);
      renderer.addVertex(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset)
          .setColor(r, g, b, a).setUv(u1, v2).setLight(light);
      renderer.addVertex(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS,
          TANK_THICKNESS + offset).setColor(r, g, b, a).setUv(u1, v1).setLight(light);

      //WEAST
      renderer.addVertex(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS,
          margin - TANK_THICKNESS).setColor(r, g, b, a).setUv(u2, v1).setLight(light);
      renderer.addVertex(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS)
          .setColor(r, g, b, a).setUv(u2, v2).setLight(light);
      renderer.addVertex(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset)
          .setColor(r, g, b, a).setUv(u1, v2).setLight(light);
      renderer.addVertex(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS,
          TANK_THICKNESS + offset).setColor(r, g, b, a).setUv(u1, v1).setLight(light);

      //EAST
      renderer.addVertex(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS,
          TANK_THICKNESS + offset).setColor(r, g, b, a).setUv(u1, v1).setLight(light);
      renderer.addVertex(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset)
          .setColor(r, g, b, a).setUv(u1, v2).setLight(light);
      renderer.addVertex(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS)
          .setColor(r, g, b, a).setUv(u2, v2).setLight(light);
      renderer.addVertex(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS,
          margin - TANK_THICKNESS).setColor(r, g, b, a).setUv(u2, v1).setLight(light);
    }
  }
}
