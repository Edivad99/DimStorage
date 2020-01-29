package edivad.dimstorage.client.screen;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.container.ContainerDimChest;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateBlock;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tools.Translate;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenDimChest extends PanelScreen<ContainerDimChest> {

	private TileEntityDimChest ownerTile;

	public ScreenDimChest(ContainerDimChest container, PlayerInventory invPlayer, ITextComponent text)
	{
		super(container, invPlayer, text, new ResourceLocation(Main.MODID, "textures/gui/dimchest.png"), container.isOpen);
		this.ownerTile = container.owner;
	}

	@Override
	protected void actions(Actions action)
	{
		switch (action)
		{
			case OWNER:
				ownerTile.swapOwner();
				break;

			case FREQ:
				int prevChannel = ownerTile.frequency.getChannel();
				try
				{
					int freq = Math.abs(Integer.parseInt(freqTextField.getText()));
					ownerTile.setFreq(ownerTile.frequency.copy().setChannel(freq));
				}
				catch(Exception e)
				{
					freqTextField.setText(String.valueOf(prevChannel));
				}
				break;

			case LOCK:
				ownerTile.swapLocked();
				break;
		}
		PacketHandler.INSTANCE.sendToServer(new UpdateBlock(ownerTile));
	}

	@Override
	protected Frequency getTileFrequency()
	{
		return ownerTile.frequency;
	}

	@Override
	protected boolean isLocked()
	{
		return ownerTile.isLocked();
	}

	@Override
	protected String getName()
	{
		return Translate.translateToLocal("block." + Main.MODID + ".dimensional_chest");
	}
}
