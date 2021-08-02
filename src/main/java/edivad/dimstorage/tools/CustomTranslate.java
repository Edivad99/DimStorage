package edivad.dimstorage.tools;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;

import java.util.regex.Pattern;

public class CustomTranslate {

    private static final Pattern COMPILE = Pattern.compile("@", Pattern.LITERAL);

    public static TextComponent translateToLocal(String key) {
        String translated = I18n.get(key);
        translated = COMPILE.matcher(translated).replaceAll("\u00a7");
        return new TextComponent(translated);
    }
}
