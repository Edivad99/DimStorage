package edivad.dimstorage.tools;

import edivad.dimstorage.Main;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class Message {

	public static enum Messages {
		ACCESSDENIED, BINDINGCOMPLETE, NOTLINKED
	}

	public static void sendChatMessage(PlayerEntity player, Messages message)
	{
		if(player.isServerWorld())
			return;

		StringTextComponent messageToSend = null;
		switch (message)
		{
			case ACCESSDENIED:
				messageToSend = new StringTextComponent(TextFormatting.RED + Translate.translateToLocal("message." + Main.MODID + ".accessDenied"));
				break;

			case BINDINGCOMPLETE:
				messageToSend = new StringTextComponent(TextFormatting.GREEN + Translate.translateToLocal("message." + Main.MODID + ".bindingComplete"));
				break;

			case NOTLINKED:
				messageToSend = new StringTextComponent(TextFormatting.RED + Translate.translateToLocal("message." + Main.MODID + ".notLinked"));
				break;
		}
		player.sendMessage(messageToSend);
	}
}
