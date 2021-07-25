package edivad.dimstorage.client.screen.element.textfield;

import edivad.dimstorage.api.Frequency;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.TextComponent;

public class FrequencyText extends EditBox {

    public FrequencyText(int width, int height, Frequency frequency)
    {
        super(Minecraft.getInstance().font, width, height, 64, 15, new TextComponent(""));
        setMaxLength(3);
        setVisible(true);
        setFocused(false);
        setValue(String.valueOf(frequency.getChannel()));
    }
}
