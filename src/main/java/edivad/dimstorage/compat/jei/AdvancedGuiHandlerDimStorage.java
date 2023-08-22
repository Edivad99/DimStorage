package edivad.dimstorage.compat.jei;

import java.util.List;
import edivad.dimstorage.client.screen.pattern.PanelScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;

public class AdvancedGuiHandlerDimStorage<Panel extends PanelScreen<?>> implements
    IGuiContainerHandler<Panel> {

  @Override
  public List<Rect2i> getGuiExtraAreas(Panel containerScreen) {
    return containerScreen.getAreas();
  }
}
