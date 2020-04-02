package edivad.dimstorage.client.render.tile;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import edivad.dimstorage.storage.DimTankStorage;
import edivad.dimstorage.tile.TileEntityDimTank;
import edivad.dimstorage.tools.extra.fluid.FluidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class RenderTileDimTank extends TileEntityRenderer<TileEntityDimTank> {

	private static final float TANK_THICKNESS = 0.1f;
	
	public RenderTileDimTank(TileEntityRendererDispatcher dispatcher)
	{
		super(dispatcher);
	}
	
	@Override
	public void render(TileEntityDimTank tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		if(tileEntityIn == null || tileEntityIn.isRemoved())
			return;
		
		matrixStackIn.push();
		RenderSystem.disableBlend();
		RenderSystem.translated(0, 0, 0);
		Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		renderFluid(tileEntityIn);
		matrixStackIn.pop();
	}

//	@Override
//	public void render(TileEntityDimTank tileEntity, double x, double y, double z, float partialTicks, int destroyStage)
//	{
//		super.render(tileEntity, x, y, z, partialTicks, destroyStage);
//		GlStateManager.pushMatrix();
//		//GlStateManager.disableRescaleNormal();
//		//GlStateManager.color4f(1, 1, 1, 1);
//		GlStateManager.disableBlend();
//		GlStateManager.translated(x, y, z);
//		bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
//		if(tileEntity != null)
//			renderFluid(tileEntity);
//		GlStateManager.popMatrix();
//
//	}

	private void renderFluid(@Nonnull TileEntityDimTank tank)
	{
		FluidStack fluid = tank.liquidState.clientLiquid;
		if(fluid == null)
			return;

		float scale = (1.0f - TANK_THICKNESS / 2 - TANK_THICKNESS) * fluid.getAmount() / (DimTankStorage.CAPACITY);
		if(scale > 0.0f)
		{
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder renderer = tessellator.getBuffer();
			TextureAtlasSprite sprite = FluidUtils.getFluidTexture(fluid);
			
			if(sprite==null)
				return;
			RenderHelper.disableStandardItemLighting();
			renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

			float u1 = sprite.getMinU();
			float v1 = sprite.getMinV();
			float u2 = sprite.getMaxU();
			float v2 = sprite.getMaxV();
			

			float margin = 0.9f;
			float offset = 0.1f;

			int color = fluid.getFluid().getAttributes().getColor(fluid);
			float r = FluidUtils.getRed(color);
			float g = FluidUtils.getGreen(color);
			float b = FluidUtils.getBlue(color);
			float a = FluidUtils.getAlpha(color);
			//System.out.println(scale);

			// Top
			renderer.pos(TANK_THICKNESS + offset, scale + TANK_THICKNESS, TANK_THICKNESS + offset).tex(u1, v1).color(r, g, b, a).endVertex();
			renderer.pos(TANK_THICKNESS + offset, scale + TANK_THICKNESS, margin - TANK_THICKNESS).tex(u1, v2).color(r, g, b, a).endVertex();
			renderer.pos(margin - TANK_THICKNESS, scale + TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v2).color(r, g, b, a).endVertex();
			renderer.pos(margin - TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS + offset).tex(u2, v1).color(r, g, b, a).endVertex();

			// Bottom
			renderer.pos(margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset).tex(u2, v1).color(r, g, b, a).endVertex();
			renderer.pos(margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v2).color(r, g, b, a).endVertex();
			renderer.pos(TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS).tex(u1, v2).color(r, g, b, a).endVertex();
			renderer.pos(TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset).tex(u1, v1).color(r, g, b, a).endVertex();

			// Sides
			//NORTH
			renderer.pos(TANK_THICKNESS + offset, scale + TANK_THICKNESS, margin - TANK_THICKNESS).tex(u1, v1).color(r, g, b, a).endVertex();
			renderer.pos(TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS).tex(u1, v2).color(r, g, b, a).endVertex();
			renderer.pos(margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v2).color(r, g, b, a).endVertex();
			renderer.pos(margin - TANK_THICKNESS, scale + TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v1).color(r, g, b, a).endVertex();

			//SOUTH
			renderer.pos(margin - TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS + offset).tex(u2, v1).color(r, g, b, a).endVertex();
			renderer.pos(margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset).tex(u2, v2).color(r, g, b, a).endVertex();
			renderer.pos(TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset).tex(u1, v2).color(r, g, b, a).endVertex();
			renderer.pos(TANK_THICKNESS + offset, scale + TANK_THICKNESS, TANK_THICKNESS + offset).tex(u1, v1).color(r, g, b, a).endVertex();

			//WEAST
			renderer.pos(margin - TANK_THICKNESS, scale + TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v1).color(r, g, b, a).endVertex();
			renderer.pos(margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v2).color(r, g, b, a).endVertex();
			renderer.pos(margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset).tex(u1, v2).color(r, g, b, a).endVertex();
			renderer.pos(margin - TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS + offset).tex(u1, v1).color(r, g, b, a).endVertex();

			//EAST
			renderer.pos(TANK_THICKNESS + offset, scale + TANK_THICKNESS, TANK_THICKNESS + offset).tex(u1, v1).color(r, g, b, a).endVertex();
			renderer.pos(TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset).tex(u1, v2).color(r, g, b, a).endVertex();
			renderer.pos(TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v2).color(r, g, b, a).endVertex();
			renderer.pos(TANK_THICKNESS + offset, scale + TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v1).color(r, g, b, a).endVertex();

			tessellator.draw();
			RenderHelper.enableStandardItemLighting();
		}
	}
	
    
}
