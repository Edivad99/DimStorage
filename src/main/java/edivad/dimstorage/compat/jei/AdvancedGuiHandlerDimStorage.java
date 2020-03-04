package edivad.dimstorage.compat.jei;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edivad.dimstorage.client.screen.pattern.PanelScreen;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rectangle2d;

@SuppressWarnings("rawtypes")
public class AdvancedGuiHandlerDimStorage implements IGuiContainerHandler<PanelScreen> {

	public AdvancedGuiHandlerDimStorage()
	{
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Rectangle2d> getGuiExtraAreas(PanelScreen containerScreen)
	{
		return containerScreen.getAreas();
	}

	@Override
	public Object getIngredientUnderMouse(PanelScreen containerScreen, double mouseX, double mouseY)
	{
		return null;
	}

	@Override
	public Collection<IGuiClickableArea> getGuiClickableAreas(PanelScreen containerScreen)
	{
		return Collections.emptyList();
	}
}
