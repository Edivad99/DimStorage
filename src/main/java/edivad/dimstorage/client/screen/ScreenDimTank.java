package edivad.dimstorage.client.screen;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.container.ContainerDimTank;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.tank.UpdateDimTank;
import edivad.dimstorage.storage.DimTankStorage;
import edivad.dimstorage.tile.TileEntityDimTank;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public class ScreenDimTank extends PanelScreen<ContainerDimTank> {

	private TileEntityDimTank ownerTile;

	public ScreenDimTank(ContainerDimTank container, PlayerInventory invPlayer, ITextComponent text)
	{
		super(container, invPlayer, text, new ResourceLocation(Main.MODID, "textures/gui/dimtank.png"), container.isOpen);
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
			default:
				return;
		}
		PacketHandler.INSTANCE.sendToServer(new UpdateDimTank(ownerTile));
	}

	@Override
	protected Frequency getTileFrequency()
	{
		return ownerTile.frequency;
	}

	@Override
	protected boolean isLocked()
	{
		return ownerTile.locked;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		FluidStack liquidStack = ownerTile.liquidState.c_liquid;
		FluidAttributes liquidAttributes = liquidStack.getFluid().getAttributes();

		if(!liquidStack.getFluid().isEquivalentTo(Fluids.EMPTY))
		{
			this.font.drawString("Liquid: " + liquidStack.getDisplayName().getFormattedText(), 50, 25, 4210752);
			this.font.drawString("Amount: " + liquidStack.getAmount() + " mB", 50, 35, 4210752);
			this.font.drawString("Temperature: " + (liquidAttributes.getTemperature() - 273) + " °C", 50, 45, 4210752);
			this.font.drawString("Luminosity: " + liquidAttributes.getLuminosity(), 50, 55, 4210752);
			this.font.drawString("Gaseous: " + liquidAttributes.isGaseous(), 50, 65, 4210752);
		}
		else
		{
			this.font.drawString("Liquid: Empty", 50, 25, 4210752);
		}

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		super.drawGuiContainerBackgroundLayer(f, i, j);

		FluidStack fluid = ownerTile.liquidState.c_liquid;
		int z = this.getFluidScaled(60, fluid.getAmount());
		TextureAtlasSprite fluidTexture = this.minecraft.getTextureMap().getSprite(fluid.getFluid().getAttributes().getStillTexture());

		this.minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		ScreenDimTank.blit(this.guiLeft + 11, this.guiTop + 21 + z, 176, 16, 60 - z, fluidTexture);
	}

	private int getFluidScaled(int pixels, int currentLiquidAmount)
	{
		int maxLiquidAmount = DimTankStorage.CAPACITY;
		int x = currentLiquidAmount * pixels / maxLiquidAmount;
		return pixels - x;
	}

	@Override
	protected boolean isCollecting()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
