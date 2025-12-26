package edivad.dimstorage.compat.jei;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.client.screen.pattern.PanelScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.Identifier;

@JeiPlugin
public class DimStorageJEI implements IModPlugin {

  @Override
  public Identifier getPluginUid() {
    return DimStorage.id("jei_plugin");
  }

  @Override
  public void registerGuiHandlers(IGuiHandlerRegistration registration) {
    registration.addGuiContainerHandler(PanelScreen.class, new AdvancedGuiHandlerDimStorage<>());
  }
}
