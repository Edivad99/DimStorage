package edivad.dimstorage.tools;

import java.util.regex.Pattern;

import net.minecraft.client.resources.I18n;

public class Translate {

	private static final Pattern COMPILE = Pattern.compile("@", Pattern.LITERAL);

	public static String translateToLocal(String key)
	{
		String translated = I18n.format(key);
		translated = COMPILE.matcher(translated).replaceAll("\u00a7");
		return translated;
	}

	public static String translateToLocal(String key, Object... parameters)
	{
		String translated = I18n.format(key, parameters);
		translated = COMPILE.matcher(translated).replaceAll("\u00a7");
		return translated;
	}
}
