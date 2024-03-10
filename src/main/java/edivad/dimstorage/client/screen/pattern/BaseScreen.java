package edivad.dimstorage.client.screen.pattern;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
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
    int x = (this.width - this.getXSize()) / 2;
    int y = (this.height - this.getYSize()) / 2;
    guiGraphics.blit(background, x, y, 0, 0, this.getXSize(), this.getYSize());
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    super.render(guiGraphics, mouseX, mouseY, partialTicks);
    this.renderTooltip(guiGraphics, mouseX, mouseY);
  }
}
