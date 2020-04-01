package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimChest;
import edivad.dimstorage.network.packet.UpdateDimTank;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tile.TileEntityDimTank;
import edivad.dimstorage.tile.TileFrequencyOwner;
import edivad.dimstorage.tools.Config;
import net.minecraft.client.gui.widget.button.Button;

public class OwnerButton extends Button {

	private TileFrequencyOwner owner;

	public OwnerButton(int width, int height, TileFrequencyOwner owner)
	{
		super(width, height, 64, 20, owner.frequency.getOwner(), b -> {});
		this.owner = owner;
		this.active = Config.DIMCHEST_ALLOWPRIVATENETWORK.get();
	}

	@Override
	public void onPress()
	{
		if(owner instanceof TileEntityDimChest)
		{
			TileEntityDimChest chest = (TileEntityDimChest) owner;
			chest.swapOwner();
			PacketHandler.INSTANCE.sendToServer(new UpdateDimChest(chest));
		}
		else if(owner instanceof TileEntityDimTank)
		{
			TileEntityDimTank tank = (TileEntityDimTank) owner;
			tank.swapOwner();
			PacketHandler.INSTANCE.sendToServer(new UpdateDimTank(tank));
		}
	}
}
