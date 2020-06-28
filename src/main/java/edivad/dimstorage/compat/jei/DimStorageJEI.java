package edivad.dimstorage.compat.jei;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.screen.pattern.PanelScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class DimStorageJEI implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid()
    {
        return new ResourceLocation(Main.MODID, "jei_plugin");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        registration.addGuiContainerHandler(PanelScreen.class, new AdvancedGuiHandlerDimStorage());
    }

}
