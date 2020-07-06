package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.Main;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

public class ChangeButton extends Button {

    public ChangeButton(int width, int height, Button.IPressable onPress)
    {
        super(width, height, 64, 20, new TranslationTextComponent("gui." + Main.MODID + ".change").getString(), onPress);
    }
}
