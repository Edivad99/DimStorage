//package edivad.dimstorage.compat.jei;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//
//import edivad.dimstorage.client.screen.pattern.PanelScreen;
//import mezz.jei.api.gui.handlers.IGuiClickableArea;
//import mezz.jei.api.gui.handlers.IGuiContainerHandler;
//import net.minecraft.client.renderer.Rectangle2d;
//
//public class AdvancedGuiHandlerDimStorage <Panel extends PanelScreen<?>> implements IGuiContainerHandler<Panel> {
//
//    @Override
//    public List<Rectangle2d> getGuiExtraAreas(Panel containerScreen)
//    {
//        return containerScreen.getAreas();
//    }
//
//    @Override
//    public Object getIngredientUnderMouse(Panel containerScreen, double mouseX, double mouseY)
//    {
//        return null;
//    }
//
//    @Override
//    public Collection<IGuiClickableArea> getGuiClickableAreas(Panel containerScreen, double mouseX, double mouseY)
//    {
//        return Collections.emptyList();
//    }
//}
