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
        this.xSize = 176;
        this.ySize = 220;
    }

    @Override
    protected void func_230450_a_(MatrixStack mStack, float partialTicks, int mouseX, int mouseY)//drawGuiContainerBackgroundLayer
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_230706_i_.getTextureManager().bindTexture(background);//this.minecraft
    }

    @Override
    protected void func_230451_b_(MatrixStack mStack, int mouseX, int mouseY)//drawGuiContainerForegroundLayer
    {
        this.field_230712_o_.func_238422_b_(mStack, this.func_231171_q_(), 8, 6, 4210752);//this.font.drawString
        this.field_230712_o_.func_238422_b_(mStack, new TranslationTextComponent("container.inventory"), 8, 128, 4210752);
    }

    @Override
    public void func_230430_a_(MatrixStack mStack, int mouseX, int mouseY, float partialTicks)//render
    {
        this.func_230446_a_(mStack);//this.renderBackground();
        super.func_230430_a_(mStack, mouseX, mouseY, partialTicks);//super.render(mStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(mStack, mouseX, mouseY);//this.renderHoveredToolTip(mStack, mouseX, mouseY);
    }
}
