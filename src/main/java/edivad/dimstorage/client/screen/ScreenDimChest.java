package edivad.dimstorage.client.screen;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.screen.element.button.CollectButton;
import edivad.dimstorage.client.screen.pattern.FrequencyScreen;
import edivad.dimstorage.container.ContainerDimChest;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateBlock;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenDimChest extends FrequencyScreen<ContainerDimChest> {

	private TileEntityDimChest ownerTile;
	
	public ScreenDimChest(ContainerDimChest container, PlayerInventory invPlayer, ITextComponent text)
	{
		super(container, container.owner, invPlayer, text, new ResourceLocation(Main.MODID, "textures/gui/dimchest.png"), container.isOpen);
		this.ownerTile = container.owner;
	}

	@Override
	protected void actions(Actions action)
	{
		if(action == Actions.OWNER)
			ownerTile.swapOwner();
		else if(action == Actions.LOCK)
			ownerTile.swapLocked();
		else if(action == Actions.COLLECT)
			ownerTile.swapCollect();
		else if(action == Actions.FREQ)
		{
			int prevChannel = ownerTile.frequency.getChannel();
			try
			{
				int newFreq = getFrequencyText();
				ownerTile.setFreq(ownerTile.frequency.copy().setChannel(newFreq));
			}
			catch(Exception e)
			{
				setFrequencyText(prevChannel);
			}
		}
		PacketHandler.INSTANCE.sendToServer(new UpdateBlock(ownerTile));
	}
	
	@Override
	protected void init()
	{
		super.init();

		addComponent(new CollectButton(width / 2 + 95, height / 2 + 75, ownerTile.collect, button -> actions(Actions.COLLECT)));

		drawSettings(drawSettings);
	}
}
