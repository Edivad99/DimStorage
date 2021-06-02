package edivad.dimstorage.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.screen.pattern.BaseScreen;
import edivad.dimstorage.container.ContainerDimTablet;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenDimTablet extends BaseScreen<ContainerDimTablet> {

    public ScreenDimTablet(ContainerDimTablet container, PlayerInventory invPlayer, ITextComponent text)
    {
        super(container, invPlayer, text, new ResourceLocation(Main.MODID, "textures/gui/dimchest.png"));
    }

    @Override
    protected void renderBg(MatrixStack mStack, float partialTicks, int mouseX, int mouseY)
    {
        super.renderBg(mStack, partialTicks, mouseX, mouseY);
        this.blit(mStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight + 2);//Space to see the border
    }
}
