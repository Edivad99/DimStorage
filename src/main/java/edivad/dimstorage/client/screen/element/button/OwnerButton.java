package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.tools.Config;
import net.minecraft.client.gui.widget.button.Button;

public class OwnerButton extends Button {

	public OwnerButton(int width, int height, String owner, Button.IPressable onPress)
	{
		super(width / 2 + 95, height / 2 - 57, 64, 20, owner, onPress);
		this.active = Config.DIMCHEST_ALLOWPRIVATENETWORK.get();
	}
}
