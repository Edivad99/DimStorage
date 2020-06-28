package edivad.dimstorage.client.screen.element.textfield;

import edivad.dimstorage.api.Frequency;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

public class FrequencyText extends TextFieldWidget {

    public FrequencyText(int width, int height, Frequency frequency)
    {
        super(Minecraft.getInstance().fontRenderer, width, height, 64, 15, new StringTextComponent(""));
        setMaxStringLength(3);
        setVisible(true);
        setFocused(false);
        setText(String.valueOf(frequency.getChannel()));
    }
}
