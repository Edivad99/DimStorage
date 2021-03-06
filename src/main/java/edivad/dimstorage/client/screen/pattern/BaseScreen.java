package edivad.dimstorage.client.screen.pattern;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class BaseScreen<T extends Container> extends ContainerScreen<T> {

    private ResourceLocation background;

    public BaseScreen(T container, PlayerInventory invPlayer, ITextComponent text, ResourceLocation background)
    {
        super(container, invPlayer, text);
        this.background = background;
        this.imageWidth = 176;
        this.imageHeight = 220;
    }

    @Override
    protected void renderBg(MatrixStack mStack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(background);
    }

    @Override
    protected void renderLabels(MatrixStack mStack, int mouseX, int mouseY)
    {
        this.font.draw(mStack, this.getTitle(), 8, 6, 4210752);
        this.font.draw(mStack, new TranslationTextComponent("container.inventory"), 8, 128, 4210752);
    }

    @Override
    public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(mStack);
        super.render(mStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(mStack, mouseX, mouseY);
    }
}
