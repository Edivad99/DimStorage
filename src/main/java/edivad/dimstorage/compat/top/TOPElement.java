package edivad.dimstorage.compat.top;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import edivad.dimstorage.tools.utils.GuiUtils;
import mcjty.theoneprobe.api.IElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.Nullable;

public abstract class TOPElement implements IElement {

    private final int borderColor;
    private final int textColor;

    protected TOPElement(int borderColor, int textColor) {
        this.borderColor = borderColor;
        this.textColor = textColor;
    }

    @Override
    public void render(PoseStack poseStack, int x, int y) {
        int width = getWidth();
        int height = getHeight();
        GuiComponent.fill(poseStack, x, y, x + width - 1, y + 1, borderColor);
        GuiComponent.fill(poseStack, x, y, x + 1, y + height - 1, borderColor);
        GuiComponent.fill(poseStack, x + width - 1, y, x + width, y + height - 1, borderColor);
        GuiComponent.fill(poseStack, x, y + height - 1, x + width, y + height, borderColor);
        TextureAtlasSprite icon = getIcon();
        if(icon != null) {
            int scale = getScaledLevel(width - 2);
            if(scale > 0) {
                boolean colored = applyRenderColor();
                GuiUtils.drawTiledSprite(x + 1, y + 1, height - 2, scale, height - 2, icon, 16, 16, 0);
                if(colored) {
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                }
            }
        }
        renderScaledText(poseStack, Minecraft.getInstance().font, x + 4, y + 3, textColor, getWidth() - 8, getText());
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 13;
    }

    public abstract int getScaledLevel(int level);

    @Nullable
    public abstract TextureAtlasSprite getIcon();

    public abstract TextComponent getText();

    protected boolean applyRenderColor() {
        return false;
    }

    protected static void renderScaledText(PoseStack poseStack, Font font, int x, int y, int color, int maxWidth, TextComponent component) {
        String text = component.getString();
        int length = font.width(text);
        if(length <= maxWidth) {
            font.draw(poseStack, text, x, y, color);
        }
        else {
            float scale = (float) maxWidth / length;
            float reverse = 1 / scale;
            float yAdd = 4 - (scale * 8) / 2F;
            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
            font.draw(poseStack, text, (int) (x * reverse), (int) ((y * reverse) + yAdd), color);
            poseStack.popPose();
        }
        //Make sure the color does not leak from having drawn the string
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
