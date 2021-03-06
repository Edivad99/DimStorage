package edivad.dimstorage.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.screen.element.button.AutoEjectButton;
import edivad.dimstorage.client.screen.pattern.FrequencyScreen;
import edivad.dimstorage.container.ContainerDimTank;
import edivad.dimstorage.storage.DimTankStorage;
import edivad.dimstorage.tile.TileEntityDimTank;
import edivad.dimstorage.tools.utils.FluidUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public class ScreenDimTank extends FrequencyScreen<ContainerDimTank> {

    private String liquid, amount, temperature, luminosity, gaseous, empty, yes, no;

    public ScreenDimTank(ContainerDimTank container, PlayerInventory invPlayer, ITextComponent text)
    {
        super(container, container.owner, invPlayer, text, new ResourceLocation(Main.MODID, "textures/gui/dimtank.png"), container.isOpen);
    }

    @Override
    protected void init()
    {
        super.init();

        addComponent(new AutoEjectButton(width / 2 + 95, height / 2 + 75, (TileEntityDimTank) tileOwner));

        drawSettings(drawSettings);

        liquid = new TranslationTextComponent("gui." + Main.MODID + ".liquid").getString();
        amount = new TranslationTextComponent("gui." + Main.MODID + ".amount").getString();
        temperature = new TranslationTextComponent("gui." + Main.MODID + ".temperature").getString();
        luminosity = new TranslationTextComponent("gui." + Main.MODID + ".luminosity").getString();
        gaseous = new TranslationTextComponent("gui." + Main.MODID + ".gas").getString();
        empty = new TranslationTextComponent("gui." + Main.MODID + ".empty").getString();
        yes = new TranslationTextComponent("gui." + Main.MODID + ".yes").getString();
        no = new TranslationTextComponent("gui." + Main.MODID + ".no").getString();
    }

    @Override
    protected void renderLabels(MatrixStack mStack, int mouseX, int mouseY)
    {
        super.renderLabels(mStack, mouseX, mouseY);
        FluidStack liquidStack = ((TileEntityDimTank) tileOwner).liquidState.clientLiquid;

        if(!liquidStack.isEmpty())
        {
            FluidAttributes liquidAttributes = liquidStack.getFluid().getAttributes();
            String liquidName = liquidStack.getDisplayName().getString();
            this.font.draw(mStack, liquid + " " + liquidName.substring(0, Math.min(14, liquidName.length())), 50, 25, 4210752);
            this.font.draw(mStack, amount + " " + liquidStack.getAmount() + " mB", 50, 35, 4210752);
            this.font.draw(mStack, temperature + " " + (liquidAttributes.getTemperature() - 273) + "C", 50, 45, 4210752);
            this.font.draw(mStack, luminosity + " " + liquidAttributes.getLuminosity(), 50, 55, 4210752);
            this.font.draw(mStack, gaseous + " " + (liquidAttributes.isGaseous() ? yes : no), 50, 65, 4210752);
        }
        else
        {
            this.font.draw(mStack, liquid + " " + empty, 50, 25, 4210752);
        }

    }

    @Override
    protected void renderBg(MatrixStack mStack, float partialTicks, int mouseX, int mouseY)
    {
        super.renderBg(mStack, partialTicks, mouseX, mouseY);

        FluidStack fluid = ((TileEntityDimTank) tileOwner).liquidState.clientLiquid;
        int z = getFluidScaled(60, fluid.getAmount());
        TextureAtlasSprite fluidTexture = FluidUtils.getFluidTexture(fluid);

        this.minecraft.getTextureManager().bind(PlayerContainer.BLOCK_ATLAS);

        FluidUtils.color(FluidUtils.getLiquidColorWithBiome(fluid, ((TileEntityDimTank) tileOwner)));
        ScreenDimTank.blit(mStack, this.leftPos + 11, this.topPos + 21 + z, 176, 16, 60 - z, fluidTexture);
    }

    private static int getFluidScaled(int pixels, int currentLiquidAmount)
    {
        int maxLiquidAmount = DimTankStorage.CAPACITY;
        int x = currentLiquidAmount * pixels / maxLiquidAmount;
        return pixels - x;
    }
}
