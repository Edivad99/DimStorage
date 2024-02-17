package edivad.dimstorage.client.screen;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.client.screen.pattern.BaseScreen;
import edivad.dimstorage.menu.DimTabletMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ScreenDimTablet extends BaseScreen<DimTabletMenu> {

  private static final ResourceLocation DIMTABLET_GUI =
      DimStorage.rl("textures/gui/dimchest.png");

  public ScreenDimTablet(DimTabletMenu container, Inventory inventory, Component text) {
    super(container, inventory, text, DIMTABLET_GUI);
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
    guiGraphics.blit(DIMTABLET_GUI, this.leftPos, this.topPos, 0, 0, this.imageWidth,
        this.imageHeight + 2);//Space to see the border
  }
}
