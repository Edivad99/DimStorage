package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.Main;
import edivad.dimstorage.tools.Translate;
import net.minecraft.client.gui.widget.button.Button;

public class ChangeButton extends Button {

	public ChangeButton(int width, int height, Button.IPressable onPress)
	{
		super(width, height, 64, 20, Translate.translateToLocal("gui." + Main.MODID + ".change"), onPress);
	}
}