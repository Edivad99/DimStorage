package edivad.dimstorage.tools.utils;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GuiUtils {

	public static void drawTiledSprite(int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int zLevel)
	{
		if(desiredWidth == 0 || desiredHeight == 0 || textureWidth == 0 || textureHeight == 0)
		{
			return;
		}
		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		int xTileCount = desiredWidth / textureWidth;
		int xRemainder = desiredWidth - (xTileCount * textureWidth);
		int yTileCount = desiredHeight / textureHeight;
		int yRemainder = desiredHeight - (yTileCount * textureHeight);
		int yStart = yPosition + yOffset;
		float uMin = sprite.getMinU();
		float uMax = sprite.getMaxU();
		float vMin = sprite.getMinV();
		float vMax = sprite.getMaxV();
		float uDif = uMax - uMin;
		float vDif = vMax - vMin;
		GlStateManager.enableBlend();
		GlStateManager.enableAlphaTest();
		BufferBuilder vertexBuffer = Tessellator.getInstance().getBuffer();
		vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		for(int xTile = 0; xTile <= xTileCount; xTile++)
		{
			int width = (xTile == xTileCount) ? xRemainder : textureWidth;
			if(width == 0)
			{
				break;
			}
			int x = xPosition + (xTile * textureWidth);
			int maskRight = textureWidth - width;
			int shiftedX = x + textureWidth - maskRight;
			float uMaxLocal = uMax - (uDif * maskRight / textureWidth);
			for(int yTile = 0; yTile <= yTileCount; yTile++)
			{
				int height = (yTile == yTileCount) ? yRemainder : textureHeight;
				if(height == 0)
				{
					//Note: We don't want to fully break out because our height will be zero if we are looking to
					// draw the remainder, but there is no remainder as it divided evenly
					break;
				}
				int y = yStart - ((yTile + 1) * textureHeight);
				int maskTop = textureHeight - height;
				float vMaxLocal = vMax - (vDif * maskTop / textureHeight);
				vertexBuffer.pos(x, y + textureHeight, zLevel).tex(uMin, vMaxLocal).endVertex();
				vertexBuffer.pos(shiftedX, y + textureHeight, zLevel).tex(uMaxLocal, vMaxLocal).endVertex();
				vertexBuffer.pos(shiftedX, y + maskTop, zLevel).tex(uMaxLocal, vMin).endVertex();
				vertexBuffer.pos(x, y + maskTop, zLevel).tex(uMin, vMin).endVertex();
			}
		}
		vertexBuffer.finishDrawing();
		new WorldVertexBufferUploader().draw(vertexBuffer);
		GlStateManager.disableAlphaTest();
		GlStateManager.disableBlend();
	}
}