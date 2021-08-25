package edivad.dimstorage.compat.jei;

import edivad.dimstorage.client.screen.pattern.PanelScreen;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AdvancedGuiHandlerDimStorage<Panel extends PanelScreen<?>> implements IGuiContainerHandler<Panel> {

    @Override
    public List<Rect2i> getGuiExtraAreas(Panel containerScreen) {
        return containerScreen.getAreas();
    }

    @Override
    public Object getIngredientUnderMouse(Panel containerScreen, double mouseX, double mouseY) {
        return null;
    }

    @Override
    public Collection<IGuiClickableArea> getGuiClickableAreas(Panel containerScreen, double mouseX, double mouseY) {
        return Collections.emptyList();
    }
}
