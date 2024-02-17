package edivad.dimstorage.client.screen.pattern;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class BaseScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

  protected ResourceLocation background;

  public BaseScreen(T container, Inventory inventory, Component text, ResourceLocation background) {
    super(container, inventory, text);
    this.background = background;
    this.imageWidth = 176;
    this.imageHeight = 220;
    this.titleLabelX = 8;
    this.titleLabelY = 6;
    this.inventoryLabelX = 8;
    this.inventoryLabelY = 128;
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, background);
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
    super.render(guiGraphics, mouseX, mouseY, partialTicks);
    this.renderTooltip(guiGraphics, mouseX, mouseY);
  }
}
