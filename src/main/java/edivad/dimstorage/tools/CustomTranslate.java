package edivad.dimstorage.tools;

import java.util.regex.Pattern;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;

public class CustomTranslate {

	private static final Pattern COMPILE = Pattern.compile("@", Pattern.LITERAL);

	public static StringTextComponent translateToLocal(String key)
	{
		String translated = I18n.format(key);
		translated = COMPILE.matcher(translated).replaceAll("\u00a7");
		return new StringTextComponent(translated);
	}
}
