package edivad.dimstorage.client.screen.element.textfield;

import edivad.dimstorage.api.Frequency;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

public class FrequencyText extends TextFieldWidget {

    public FrequencyText(int width, int height, Frequency frequency)
    {
        super(Minecraft.getInstance().font, width, height, 64, 15, new StringTextComponent(""));
        setMaxLength(3);
        setVisible(true);
        setFocused(false);
        setValue(String.valueOf(frequency.getChannel()));
    }
}
