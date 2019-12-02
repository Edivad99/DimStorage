package edivad.dimstorage.tools;

import net.minecraft.client.resources.I18n;

public class Translate {

	public static String translateToLocal(String key)
	{
		return I18n.format(key);
	}
}
