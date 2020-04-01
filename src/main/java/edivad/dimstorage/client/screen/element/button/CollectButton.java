package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.Main;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimChest;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tools.Translate;
import net.minecraft.client.gui.widget.button.Button;

public class CollectButton extends Button {

	private TileEntityDimChest owner;

	public CollectButton(int width, int height, TileEntityDimChest owner)
	{
		super(width, height, 64, 20, getText(owner.collect), b -> {});
		this.owner = owner;
	}

	private static String getText(boolean isCollecting)
	{
		if(isCollecting)
			return Translate.translateToLocal("gui." + Main.MODID + ".collecting");
		else
			return Translate.translateToLocal("gui." + Main.MODID + ".idle");
	}

	@Override
	public void onPress()
	{
		owner.swapCollect();
		PacketHandler.INSTANCE.sendToServer(new UpdateDimChest(owner));
	}
}
