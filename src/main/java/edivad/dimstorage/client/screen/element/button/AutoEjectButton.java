package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.Main;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimTank;
import edivad.dimstorage.tile.TileEntityDimTank;
import edivad.dimstorage.tools.Translate;
import net.minecraft.client.gui.widget.button.Button;

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
			return Translate.translateToLocal("gui." + Main.MODID + ".eject");
		else
			return Translate.translateToLocal("gui." + Main.MODID + ".idle");
	}

	@Override
	public void onPress()
	{
		tank.swapAutoEject();
		PacketHandler.INSTANCE.sendToServer(new UpdateDimTank(tank));
	}
}
