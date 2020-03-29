package edivad.dimstorage.client.render.tile;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import edivad.dimstorage.storage.DimTankStorage;
import edivad.dimstorage.tile.TileEntityDimTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class RenderTileDimTank extends TileEntityRenderer<TileEntityDimTank> {

	private static final float TANK_THICKNESS = 0.04f;

	public RenderTileDimTank()
	{
	}

	@Override
	public void render(TileEntityDimTank tileEntity, double x, double y, double z, float partialTicks, int destroyStage)
	{
		GlStateManager.pushMatrix();
		GlStateManager.disableRescaleNormal();
		GlStateManager.color4f(1, 1, 1, 1);
		GlStateManager.disableBlend();
		GlStateManager.translatef((float) x, (float) y, (float) z);

		bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		renderFluid(tileEntity);

		GlStateManager.popMatrix();

		super.render(tileEntity, x, y, z, partialTicks, destroyStage);
	}

	private void renderFluid(TileEntityDimTank tank)
	{
		if(tank == null)
			return;

		FluidStack fluid = tank.liquidState.clientLiquid;
		if(fluid == null)
			return;

		Fluid renderFluid = fluid.getFluid();
		if(renderFluid == null)
			return;

		float scale = (1.0f - TANK_THICKNESS / 2 - TANK_THICKNESS) * fluid.getAmount() / (DimTankStorage.CAPACITY);
		if(scale > 0.0f)
		{
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder renderer = tessellator.getBuffer();
			ResourceLocation still = renderFluid.getAttributes().getStillTexture();
			TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap().getSprite(still);
			RenderHelper.disableStandardItemLighting();

			GlStateManager.color4f(1, 1, 1, .5f);
			renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

			float u1 = sprite.getMinU();
			float v1 = sprite.getMinV();
			float u2 = sprite.getMaxU();
			float v2 = sprite.getMaxV();

			float margin = 0.9f;

			// Top
			renderer.pos(TANK_THICKNESS + 0.1f, scale + TANK_THICKNESS, TANK_THICKNESS + 0.1f).tex(u1, v1).color(255, 255, 255, 128).endVertex();
			renderer.pos(TANK_THICKNESS + 0.1f, scale + TANK_THICKNESS, margin - TANK_THICKNESS).tex(u1, v2).color(255, 255, 255, 128).endVertex();
			renderer.pos(margin - TANK_THICKNESS, scale + TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v2).color(255, 255, 255, 128).endVertex();
			renderer.pos(margin - TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS + 0.1f).tex(u2, v1).color(255, 255, 255, 128).endVertex();

			// Bottom
			renderer.pos(margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + 0.1f).tex(u2, v1).color(255, 255, 255, 128).endVertex();
			renderer.pos(margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v2).color(255, 255, 255, 128).endVertex();
			renderer.pos(TANK_THICKNESS + 0.1f, TANK_THICKNESS, margin - TANK_THICKNESS).tex(u1, v2).color(255, 255, 255, 128).endVertex();
			renderer.pos(TANK_THICKNESS + 0.1f, TANK_THICKNESS, TANK_THICKNESS + 0.1f).tex(u1, v1).color(255, 255, 255, 128).endVertex();

			// Sides
			//NORTH
			renderer.pos(TANK_THICKNESS + 0.1f, scale + TANK_THICKNESS, margin - TANK_THICKNESS).tex(u1, v1).color(255, 255, 255, 128).endVertex();
			renderer.pos(TANK_THICKNESS + 0.1f, TANK_THICKNESS, margin - TANK_THICKNESS).tex(u1, v2).color(255, 255, 255, 128).endVertex();
			renderer.pos(margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v2).color(255, 255, 255, 128).endVertex();
			renderer.pos(margin - TANK_THICKNESS, scale + TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v1).color(255, 255, 255, 128).endVertex();

			//SOUTH
			renderer.pos(margin - TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS + 0.1f).tex(u2, v1).color(255, 255, 255, 128).endVertex();
			renderer.pos(margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + 0.1f).tex(u2, v2).color(255, 255, 255, 128).endVertex();
			renderer.pos(TANK_THICKNESS + 0.1f, TANK_THICKNESS, TANK_THICKNESS + 0.1f).tex(u1, v2).color(255, 255, 255, 128).endVertex();
			renderer.pos(TANK_THICKNESS + 0.1f, scale + TANK_THICKNESS, TANK_THICKNESS + 0.1f).tex(u1, v1).color(255, 255, 255, 128).endVertex();

			//WEAST
			renderer.pos(margin - TANK_THICKNESS, scale + TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v1).color(255, 255, 255, 128).endVertex();
			renderer.pos(margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v2).color(255, 255, 255, 128).endVertex();
			renderer.pos(margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + 0.1f).tex(u1, v2).color(255, 255, 255, 128).endVertex();
			renderer.pos(margin - TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS + 0.1f).tex(u1, v1).color(255, 255, 255, 128).endVertex();

			//EAST
			renderer.pos(TANK_THICKNESS + 0.1f, scale + TANK_THICKNESS, TANK_THICKNESS + 0.1f).tex(u1, v1).color(255, 255, 255, 128).endVertex();
			renderer.pos(TANK_THICKNESS + 0.1f, TANK_THICKNESS, TANK_THICKNESS + 0.1f).tex(u1, v2).color(255, 255, 255, 128).endVertex();
			renderer.pos(TANK_THICKNESS + 0.1f, TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v2).color(255, 255, 255, 128).endVertex();
			renderer.pos(TANK_THICKNESS + 0.1f, scale + TANK_THICKNESS, margin - TANK_THICKNESS).tex(u2, v1).color(255, 255, 255, 128).endVertex();

			tessellator.draw();

			RenderHelper.enableStandardItemLighting();
		}
	}
}
