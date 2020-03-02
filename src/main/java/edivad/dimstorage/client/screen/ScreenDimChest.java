package edivad.dimstorage.client.screen;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.screen.element.button.CollectButton;
import edivad.dimstorage.client.screen.pattern.FrequencyScreen;
import edivad.dimstorage.container.ContainerDimChest;
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
	protected void init()
	{
		super.init();

		addComponent(new CollectButton(width / 2 + 95, height / 2 + 75, ownerTile));

		drawSettings(drawSettings);
	}
}
