package edivad.dimstorage.client.screen.pattern;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class BaseScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

  protected Identifier background;

  public BaseScreen(T container, Inventory inventory, Component text, Identifier background) {
    super(container, inventory, text, 176, 220);
    this.background = background;
    this.titleLabelX = 8;
    this.titleLabelY = 6;
    this.inventoryLabelX = 8;
    this.inventoryLabelY = 128;
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
    super.extractBackground(graphics, mouseX, mouseY, partialTicks);
    int x = (this.width - this.getImageWidth()) / 2;
    int y = (this.height - this.getImageHeight()) / 2;
    graphics.blit(RenderPipelines.GUI_TEXTURED, background, x, y, 0, 0,
        this.getImageWidth(), this.getImageHeight(), 256, 256);
  }
}
