package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.Main;
import edivad.dimstorage.tools.Translate;
import net.minecraft.client.gui.widget.button.Button;

public class CollectButton extends Button {

	public CollectButton(int width, int height, boolean isCollecting, Button.IPressable onPress)
	{
		super(width, height, 64, 20, getText(isCollecting), onPress);
	}

	private static String getText(boolean isCollecting)
	{
		if(isCollecting)
			return Translate.translateToLocal("gui." + Main.MODID + ".collecting");
		else
			return Translate.translateToLocal("gui." + Main.MODID + ".idle");
	}
}
