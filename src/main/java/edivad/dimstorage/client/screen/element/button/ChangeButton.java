package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.tools.Translations;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ChangeButton extends Button {

    public ChangeButton(int width, int height, Button.OnPress onPress) {
        super(width, height, 64, 20, Component.translatable(Translations.CHANGE), onPress);
    }
}
