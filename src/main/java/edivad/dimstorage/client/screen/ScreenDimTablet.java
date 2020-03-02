package edivad.dimstorage.client.screen;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.screen.pattern.BaseScreen;
import edivad.dimstorage.container.ContainerDimTablet;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;;

public class ScreenDimTablet extends BaseScreen<ContainerDimTablet> {

	public ScreenDimTablet(ContainerDimTablet container, PlayerInventory invPlayer, ITextComponent text)
	{
		super(container, invPlayer, text, new ResourceLocation(Main.MODID, "textures/gui/dimchest.png"));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		super.drawGuiContainerBackgroundLayer(f, i, j);
		this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize + 2);//Space to see the border
	}
}
