package edivad.dimstorage.client.render.blockentity;

import org.jspecify.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import edivad.dimstorage.blockentity.BlockEntityDimTank;
import edivad.dimstorage.blockentity.state.DimTankRenderState;
import edivad.dimstorage.storage.DimTankStorage;
import edivad.edivadlib.tools.utils.FluidUtils;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;

public class DimTankRenderer implements BlockEntityRenderer<BlockEntityDimTank, DimTankRenderState> {

  private static final float TANK_THICKNESS = 0.1f;

  public DimTankRenderer(BlockEntityRendererProvider.Context context) {
  }

  @Override
  public DimTankRenderState createRenderState() {
    return new DimTankRenderState();
  }

  @Override
  public void extractRenderState(BlockEntityDimTank blockEntity, DimTankRenderState state,
      float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, cameraPos, crumblingOverlay);
    state.fluid = blockEntity.liquidState.clientLiquid;
    state.fluidColor = FluidUtils.getLiquidColorWithBiome(state.fluid, blockEntity);
  }

  @Override
  public void submit(DimTankRenderState state, PoseStack poseStack,
      SubmitNodeCollector collector, CameraRenderState cameraState) {
    poseStack.pushPose();
    renderFluid(state, poseStack, collector);
    poseStack.popPose();
  }

  private void renderFluid(DimTankRenderState state, PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector) {
    var fluid = state.fluid;
    if (fluid.isEmpty()) {
      return;
    }
    float scale = (1.0f - TANK_THICKNESS / 2 - TANK_THICKNESS) * fluid.getAmount() / (DimTankStorage.CAPACITY);
    if (scale > 0.0f) {
      TextureAtlasSprite sprite = FluidUtils.getFluidTexture(fluid);

      float u1 = sprite.getU0();
      float v1 = sprite.getV0();
      float u2 = sprite.getU1();
      float v2 = sprite.getV1();

      float margin = 0.9f;
      float offset = 0.1f;

      final float r = FluidUtils.getRed(state.fluidColor);
      final float g = FluidUtils.getGreen(state.fluidColor);
      final float b = FluidUtils.getBlue(state.fluidColor);
      final float a = FluidUtils.getAlpha(state.fluidColor);
      final int light = 15728880;

      submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.text(sprite.atlasLocation()),
          (pose, vertexConsumer) -> {
            // Top
            vertexConsumer.addVertex(pose, TANK_THICKNESS + offset, scale + TANK_THICKNESS,
            TANK_THICKNESS + offset).setColor(r, g, b, a).setUv(u1, v1).setLight(light);
            vertexConsumer.addVertex(pose, TANK_THICKNESS + offset, scale + TANK_THICKNESS,
            margin - TANK_THICKNESS).setColor(r, g, b, a).setUv(u1, v2).setLight(light);
            vertexConsumer.addVertex(pose, margin - TANK_THICKNESS, scale + TANK_THICKNESS,
            margin - TANK_THICKNESS).setColor(r, g, b, a).setUv(u2, v2).setLight(light);
            vertexConsumer.addVertex(pose, margin - TANK_THICKNESS, scale + TANK_THICKNESS,
            TANK_THICKNESS + offset).setColor(r, g, b, a).setUv(u2, v1).setLight(light);

            // Bottom
            vertexConsumer.addVertex(pose, margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset)
                .setColor(r, g, b, a).setUv(u2, v1).setLight(light);
            vertexConsumer.addVertex(pose, margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS)
                .setColor(r, g, b, a).setUv(u2, v2).setLight(light);
            vertexConsumer.addVertex(pose, TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS)
                .setColor(r, g, b, a).setUv(u1, v2).setLight(light);
            vertexConsumer.addVertex(pose, TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset)
                .setColor(r, g, b, a).setUv(u1, v1).setLight(light);

            // Sides
            //NORTH
            vertexConsumer.addVertex(pose, TANK_THICKNESS + offset, scale + TANK_THICKNESS,
                margin - TANK_THICKNESS).setColor(r, g, b, a).setUv(u1, v1).setLight(light);
            vertexConsumer.addVertex(pose, TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS)
                .setColor(r, g, b, a).setUv(u1, v2).setLight(light);
            vertexConsumer.addVertex(pose, margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS)
                .setColor(r, g, b, a).setUv(u2, v2).setLight(light);
            vertexConsumer.addVertex(pose, margin - TANK_THICKNESS, scale + TANK_THICKNESS,
                margin - TANK_THICKNESS).setColor(r, g, b, a).setUv(u2, v1).setLight(light);

            //SOUTH
            vertexConsumer.addVertex(pose, margin - TANK_THICKNESS, scale + TANK_THICKNESS,
                TANK_THICKNESS + offset).setColor(r, g, b, a).setUv(u2, v1).setLight(light);
            vertexConsumer.addVertex(pose, margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset)
                .setColor(r, g, b, a).setUv(u2, v2).setLight(light);
            vertexConsumer.addVertex(pose, TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset)
                .setColor(r, g, b, a).setUv(u1, v2).setLight(light);
            vertexConsumer.addVertex(pose, TANK_THICKNESS + offset, scale + TANK_THICKNESS,
                TANK_THICKNESS + offset).setColor(r, g, b, a).setUv(u1, v1).setLight(light);

            //WEAST
            vertexConsumer.addVertex(pose, margin - TANK_THICKNESS, scale + TANK_THICKNESS,
                margin - TANK_THICKNESS).setColor(r, g, b, a).setUv(u2, v1).setLight(light);
            vertexConsumer.addVertex(pose, margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS)
                .setColor(r, g, b, a).setUv(u2, v2).setLight(light);
            vertexConsumer.addVertex(pose, margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset)
                .setColor(r, g, b, a).setUv(u1, v2).setLight(light);
            vertexConsumer.addVertex(pose, margin - TANK_THICKNESS, scale + TANK_THICKNESS,
                TANK_THICKNESS + offset).setColor(r, g, b, a).setUv(u1, v1).setLight(light);

            //EAST
            vertexConsumer.addVertex(pose, TANK_THICKNESS + offset, scale + TANK_THICKNESS,
                TANK_THICKNESS + offset).setColor(r, g, b, a).setUv(u1, v1).setLight(light);
            vertexConsumer.addVertex(pose, TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset)
                .setColor(r, g, b, a).setUv(u1, v2).setLight(light);
            vertexConsumer.addVertex(pose, TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS)
                .setColor(r, g, b, a).setUv(u2, v2).setLight(light);
            vertexConsumer.addVertex(pose, TANK_THICKNESS + offset, scale + TANK_THICKNESS,
                margin - TANK_THICKNESS).setColor(r, g, b, a).setUv(u2, v1).setLight(light);
      });
    }
  }
}
