package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.Main;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimTank;
import edivad.dimstorage.tile.TileEntityDimTank;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

public class AutoEjectButton extends Button {

	private TileEntityDimTank tank;

	public AutoEjectButton(int width, int height, TileEntityDimTank tank)
	{
		super(width, height, 64, 20, getText(tank.autoEject), b -> {});
		this.tank = tank;
	}

	private static String getText(boolean autoEject)
	{
		if(autoEject)
			return new TranslationTextComponent("gui." + Main.MODID + ".eject").getString();
		else
			return new TranslationTextComponent("gui." + Main.MODID + ".idle").getString();
	}

	@Override
	public void onPress()
	{
		tank.swapAutoEject();
		PacketHandler.INSTANCE.sendToServer(new UpdateDimTank(tank));
	}
}
