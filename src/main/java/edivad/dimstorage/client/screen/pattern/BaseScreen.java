package edivad.dimstorage.client.screen.pattern;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class BaseScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    private ResourceLocation background;

    public BaseScreen(T container, Inventory invPlayer, Component text, ResourceLocation background)
    {
        super(container, invPlayer, text);
        this.background = background;
        this.imageWidth = 176;
        this.imageHeight = 220;
    }

    @Override
    protected void renderBg(PoseStack mStack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
        RenderSystem.setShaderTexture(0, background);
    }

    @Override
    protected void renderLabels(PoseStack mStack, int mouseX, int mouseY)
    {
        this.font.draw(mStack, this.getTitle(), 8, 6, 4210752);
        this.font.draw(mStack, new TranslatableComponent("container.inventory"), 8, 128, 4210752);
    }

    @Override
    public void render(PoseStack mStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(mStack);
        super.render(mStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(mStack, mouseX, mouseY);
    }
}
