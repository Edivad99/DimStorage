package edivad.dimstorage.compat.top;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import edivad.dimstorage.tools.utils.GuiUtils;
import mcjty.theoneprobe.api.IElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.text.ITextComponent;

public abstract class TOPElement implements IElement {

    private final int borderColor;
    private final int textColor;

    protected TOPElement(int borderColor, int textColor)
    {
        this.borderColor = borderColor;
        this.textColor = textColor;
    }

    @Override
    public void render(MatrixStack mStack, int x, int y)
    {
        int width = getWidth();
        int height = getHeight();
        AbstractGui.fill(mStack, x, y, x + width - 1, y + 1, borderColor);
        AbstractGui.fill(mStack, x, y, x + 1, y + height - 1, borderColor);
        AbstractGui.fill(mStack, x + width - 1, y, x + width, y + height - 1, borderColor);
        AbstractGui.fill(mStack, x, y + height - 1, x + width, y + height, borderColor);
        TextureAtlasSprite icon = getIcon();
        if(icon != null)
        {
            int scale = getScaledLevel(width - 2);
            if(scale > 0)
            {
                boolean colored = applyRenderColor();
                GuiUtils.drawTiledSprite(x + 1, y + 1, height - 2, scale, height - 2, icon, 16, 16, 0);
                if(colored)
                {
                    RenderSystem.color4f(1, 1, 1, 1);
                }
            }
        }
        renderScaledText(mStack, Minecraft.getInstance(), x + 4, y + 3, textColor, getWidth() - 8, getText());
    }

    @Override
    public int getWidth()
    {
        return 100;
    }

    @Override
    public int getHeight()
    {
        return 13;
    }

    public abstract int getScaledLevel(int level);

    @Nullable
    public abstract TextureAtlasSprite getIcon();

    public abstract ITextComponent getText();

    protected boolean applyRenderColor()
    {
        return false;
    }

    protected static void renderScaledText(MatrixStack mStack, Minecraft mc, int x, int y, int color, int maxWidth, ITextComponent component)
    {
        String text = component.getString();
        int length = mc.font.width(text);
        if(length <= maxWidth)
        {
            mc.font.draw(mStack, text, x, y, color);
        }
        else
        {
            float scale = (float) maxWidth / length;
            float reverse = 1 / scale;
            float yAdd = 4 - (scale * 8) / 2F;
            mStack.pushPose();
            mStack.scale(scale, scale, scale);
            mc.font.draw(mStack, text, (int) (x * reverse), (int) ((y * reverse) + yAdd), color);
            mStack.popPose();
        }
        //Make sure the color does not leak from having drawn the string
        RenderSystem.color4f(1, 1, 1, 1);
    }
}
