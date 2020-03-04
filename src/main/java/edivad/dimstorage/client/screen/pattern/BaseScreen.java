package edivad.dimstorage.client.screen.pattern;

import com.mojang.blaze3d.platform.GlStateManager;

import edivad.dimstorage.tools.Translate;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BaseScreen<T extends Container> extends ContainerScreen<T> {

	private ResourceLocation background;

	public BaseScreen(T container, PlayerInventory invPlayer, ITextComponent text, ResourceLocation background)
	{
		super(container, invPlayer, text);
		this.background = background;
		this.xSize = 176;
		this.ySize = 220;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(background);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.font.drawString(this.getTitle().getFormattedText(), 8, 6, 4210752);
		this.font.drawString(Translate.translateToLocal("container.inventory"), 8, 128, 4210752);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}
