package edivad.dimstorage.tools.utils;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;

public class GuiUtils {

    public static void drawTiledSprite(int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int zLevel)
    {
        if(desiredWidth == 0 || desiredHeight == 0 || textureWidth == 0 || textureHeight == 0)
        {
            return;
        }
        Minecraft.getInstance().textureManager.bind(PlayerContainer.BLOCK_ATLAS);
        int xTileCount = desiredWidth / textureWidth;
        int xRemainder = desiredWidth - (xTileCount * textureWidth);
        int yTileCount = desiredHeight / textureHeight;
        int yRemainder = desiredHeight - (yTileCount * textureHeight);
        int yStart = yPosition + yOffset;
        float uMin = sprite.getU0();
        float uMax = sprite.getU1();
        float vMin = sprite.getV0();
        float vMax = sprite.getV1();
        float uDif = uMax - uMin;
        float vDif = vMax - vMin;
        RenderSystem.enableBlend();
        RenderSystem.enableAlphaTest();
        BufferBuilder vertexBuffer = Tessellator.getInstance().getBuilder();
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
                vertexBuffer.vertex(x, y + textureHeight, zLevel).uv(uMin, vMaxLocal).endVertex();
                vertexBuffer.vertex(shiftedX, y + textureHeight, zLevel).uv(uMaxLocal, vMaxLocal).endVertex();
                vertexBuffer.vertex(shiftedX, y + maskTop, zLevel).uv(uMaxLocal, vMin).endVertex();
                vertexBuffer.vertex(x, y + maskTop, zLevel).uv(uMin, vMin).endVertex();
            }
        }
        vertexBuffer.end();
        WorldVertexBufferUploader.end(vertexBuffer);
        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();
    }
}