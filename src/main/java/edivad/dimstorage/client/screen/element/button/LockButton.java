package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.Main;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimChest;
import edivad.dimstorage.network.packet.UpdateDimTank;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tile.TileEntityDimTank;
import edivad.dimstorage.tile.TileFrequencyOwner;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

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
			return new TranslationTextComponent("gui." + Main.MODID + ".yes").getString();
		else
			return new TranslationTextComponent("gui." + Main.MODID + ".no").getString();
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
