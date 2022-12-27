package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.tools.Translations;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ChangeButton extends Button {

    public ChangeButton(int x, int y, Button.OnPress onPress) {
        super(x, y, 64, 20, Component.translatable(Translations.CHANGE), onPress, DEFAULT_NARRATION);
    }
}
