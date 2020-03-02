package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.Main;
import edivad.dimstorage.tools.Translate;
import net.minecraft.client.gui.widget.button.Button;

public class LockButton extends Button {

	public LockButton(int width, int height, boolean isLock, Button.IPressable onPress)
	{
		super(width / 2 + 95, height / 2 + 43, 64, 20, getText(isLock), onPress);
	}

	private static String getText(boolean isLock)
	{
		if(isLock)
			return Translate.translateToLocal("gui." + Main.MODID + ".yes");
		else
			return Translate.translateToLocal("gui." + Main.MODID + ".no");
	}
}
