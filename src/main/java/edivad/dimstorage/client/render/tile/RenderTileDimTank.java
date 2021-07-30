//package edivad.dimstorage.client.render.tile;
//
//import javax.annotation.Nonnull;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//
//import edivad.dimstorage.storage.DimTankStorage;
//import edivad.dimstorage.tile.TileEntityDimTank;
//import edivad.dimstorage.tools.utils.FluidUtils;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
//import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
//import com.mojang.math.Matrix4f;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.fluids.FluidStack;
//
//@OnlyIn(Dist.CLIENT)
//public class RenderTileDimTank extends BlockEntityRenderer<TileEntityDimTank> {
//
//    private static final float TANK_THICKNESS = 0.1f;
//
//    public RenderTileDimTank(BlockEntityRenderDispatcher dispatcher)
//    {
//        super(dispatcher);
//    }
//
//    @Override
//    public void render(TileEntityDimTank tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
//    {
//        if(tileEntityIn == null || tileEntityIn.isRemoved() || tileEntityIn.liquidState.clientLiquid == null)
//            return;
//
//        matrixStackIn.pushPose();
//        renderFluid(tileEntityIn, matrixStackIn, bufferIn);
//        matrixStackIn.popPose();
//    }
//
//    private void renderFluid(@Nonnull TileEntityDimTank tileEntity, PoseStack matrix, MultiBufferSource bufferIn)
//    {
//        FluidStack fluid = tileEntity.liquidState.clientLiquid;
//        float scale = (1.0f - TANK_THICKNESS / 2 - TANK_THICKNESS) * fluid.getAmount() / (DimTankStorage.CAPACITY);
//        if(scale > 0.0f)
//        {
//            Matrix4f matrix4f = matrix.last().pose();
//            TextureAtlasSprite sprite = FluidUtils.getFluidTexture(fluid);
//            if(sprite == null)
//                return;
//            VertexConsumer renderer = bufferIn.getBuffer(RenderType.text(sprite.atlas().location()));
//
//            float u1 = sprite.getU0();
//            float v1 = sprite.getV0();
//            float u2 = sprite.getU1();
//            float v2 = sprite.getV1();
//
//            float margin = 0.9f;
//            float offset = 0.1f;
//
//            int color = FluidUtils.getLiquidColorWithBiome(fluid, tileEntity);
//
//            float r = FluidUtils.getRed(color);
//            float g = FluidUtils.getGreen(color);
//            float b = FluidUtils.getBlue(color);
//            float a = FluidUtils.getAlpha(color);
//            int light = 15728880;
//
//            // Top
//            renderer.vertex(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
//            renderer.vertex(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).uv(u1, v2).uv2(light).endVertex();
//            renderer.vertex(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).uv(u2, v2).uv2(light).endVertex();
//            renderer.vertex(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).uv(u2, v1).uv2(light).endVertex();
//
//            // Bottom
//            renderer.vertex(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).uv(u2, v1).uv2(light).endVertex();
//            renderer.vertex(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).uv(u2, v2).uv2(light).endVertex();
//            renderer.vertex(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).uv(u1, v2).uv2(light).endVertex();
//            renderer.vertex(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
//
//            // Sides
//            //NORTH
//            renderer.vertex(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
//            renderer.vertex(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).uv(u1, v2).uv2(light).endVertex();
//            renderer.vertex(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).uv(u2, v2).uv2(light).endVertex();
//            renderer.vertex(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).uv(u2, v1).uv2(light).endVertex();
//
//            //SOUTH
//            renderer.vertex(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).uv(u2, v1).uv2(light).endVertex();
//            renderer.vertex(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).uv(u2, v2).uv2(light).endVertex();
//            renderer.vertex(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).uv(u1, v2).uv2(light).endVertex();
//            renderer.vertex(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
//
//            //WEAST
//            renderer.vertex(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).uv(u2, v1).uv2(light).endVertex();
//            renderer.vertex(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).uv(u2, v2).uv2(light).endVertex();
//            renderer.vertex(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).uv(u1, v2).uv2(light).endVertex();
//            renderer.vertex(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
//
//            //EAST
//            renderer.vertex(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
//            renderer.vertex(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).uv(u1, v2).uv2(light).endVertex();
//            renderer.vertex(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).uv(u2, v2).uv2(light).endVertex();
//            renderer.vertex(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).uv(u2, v1).uv2(light).endVertex();
//        }
//    }
//}
