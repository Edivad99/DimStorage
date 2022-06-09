package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.Main;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ChangeButton extends Button {

    public ChangeButton(int width, int height, Button.OnPress onPress) {
        super(width, height, 64, 20, Component.translatable("gui." + Main.MODID + ".change"), onPress);
    }
}
