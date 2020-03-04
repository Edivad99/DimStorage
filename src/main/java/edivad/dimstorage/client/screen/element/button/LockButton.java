package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.Main;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimChest;
import edivad.dimstorage.network.packet.tank.UpdateDimTank;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tile.TileEntityDimTank;
import edivad.dimstorage.tile.TileFrequencyOwner;
import edivad.dimstorage.tools.Translate;
import net.minecraft.client.gui.widget.button.Button;

public class LockButton extends Button {

	private TileFrequencyOwner owner;

	public LockButton(int width, int height, TileFrequencyOwner owner)
	{
		super(width, height, 64, 20, getText(owner.locked), b -> {});
		this.owner = owner;
	}

	private static String getText(boolean isLock)
	{
		if(isLock)
			return Translate.translateToLocal("gui." + Main.MODID + ".yes");
		else
			return Translate.translateToLocal("gui." + Main.MODID + ".no");
	}

	@Override
	public void onPress()
	{
		if(owner instanceof TileEntityDimChest)
		{
			TileEntityDimChest chest = (TileEntityDimChest) owner;
			chest.swapLocked();
			PacketHandler.INSTANCE.sendToServer(new UpdateDimChest(chest));
		}
		else if(owner instanceof TileEntityDimTank)
		{
			TileEntityDimTank tank = (TileEntityDimTank) owner;
			tank.swapLocked();
			PacketHandler.INSTANCE.sendToServer(new UpdateDimTank(tank));
		}
	}
}
