package edivad.dimstorage.client.screen.element.textfield;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;

public class FrequencyText extends TextFieldWidget {

	public FrequencyText(int width, int height, String msg)
	{
		super(Minecraft.getInstance().fontRenderer, width, height, 64, 15, msg);
		setMaxStringLength(3);
		setVisible(true);
		setFocused(false);
		setText(msg);
	}
}
