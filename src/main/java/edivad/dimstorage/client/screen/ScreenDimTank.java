package edivad.dimstorage.client.screen;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.screen.element.button.AutoEjectButton;
import edivad.dimstorage.client.screen.pattern.FrequencyScreen;
import edivad.dimstorage.container.ContainerDimTank;
import edivad.dimstorage.storage.DimTankStorage;
import edivad.dimstorage.tile.TileEntityDimTank;
import edivad.dimstorage.tools.Translate;
import edivad.dimstorage.tools.utils.FluidUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public class ScreenDimTank extends FrequencyScreen<ContainerDimTank> {

	private TileEntityDimTank ownerTile;
	private String liquid, amount, temperature, luminosity, gaseous, empty, yes, no;

	public ScreenDimTank(ContainerDimTank container, PlayerInventory invPlayer, ITextComponent text)
	{
		super(container, container.owner, invPlayer, text, new ResourceLocation(Main.MODID, "textures/gui/dimtank.png"), container.isOpen);
		this.ownerTile = container.owner;
	}

	@Override
	protected void init()
	{
		super.init();

		addComponent(new AutoEjectButton(width / 2 + 95, height / 2 + 75, ownerTile));

		drawSettings(drawSettings);

		liquid = Translate.translateToLocal("gui." + Main.MODID + ".liquid");
		amount = Translate.translateToLocal("gui." + Main.MODID + ".amount");
		temperature = Translate.translateToLocal("gui." + Main.MODID + ".temperature");
		luminosity = Translate.translateToLocal("gui." + Main.MODID + ".luminosity");
		gaseous = Translate.translateToLocal("gui." + Main.MODID + ".gas");
		empty = Translate.translateToLocal("gui." + Main.MODID + ".empty");
		yes = Translate.translateToLocal("gui." + Main.MODID + ".yes");
		no = Translate.translateToLocal("gui." + Main.MODID + ".no");
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		FluidStack liquidStack = ownerTile.liquidState.clientLiquid;

		if(!liquidStack.isEmpty())
		{
			FluidAttributes liquidAttributes = liquidStack.getFluid().getAttributes();
			String liquidName = liquidStack.getDisplayName().getFormattedText();
			this.font.drawString(liquid + " " + liquidName.substring(0, Math.min(14, liquidName.length())), 50, 25, 4210752);
			this.font.drawString(amount + " " + liquidStack.getAmount() + " mB", 50, 35, 4210752);
			this.font.drawString(temperature + " " + (liquidAttributes.getTemperature() - 273) + "C", 50, 45, 4210752);
			this.font.drawString(luminosity + " " + liquidAttributes.getLuminosity(), 50, 55, 4210752);
			this.font.drawString(gaseous + " " + (liquidAttributes.isGaseous() ? yes : no), 50, 65, 4210752);
		}
		else
		{
			this.font.drawString(liquid + " " + empty, 50, 25, 4210752);
		}

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		super.drawGuiContainerBackgroundLayer(f, i, j);

		FluidStack fluid = ownerTile.liquidState.clientLiquid;
		int z = getFluidScaled(60, fluid.getAmount());
		TextureAtlasSprite fluidTexture = FluidUtils.getFluidTexture(fluid);

		this.minecraft.getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);

		FluidUtils.color(FluidUtils.getLiquidColorWithBiome(fluid, ownerTile));
		ScreenDimTank.blit(this.guiLeft + 11, this.guiTop + 21 + z, 176, 16, 60 - z, fluidTexture);
	}

	private static int getFluidScaled(int pixels, int currentLiquidAmount)
	{
		int maxLiquidAmount = DimTankStorage.CAPACITY;
		int x = currentLiquidAmount * pixels / maxLiquidAmount;
		return pixels - x;
	}
}
