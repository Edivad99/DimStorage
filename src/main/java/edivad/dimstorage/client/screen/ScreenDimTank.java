package edivad.dimstorage.client.screen;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.client.screen.element.button.AutoEjectButton;
import edivad.dimstorage.client.screen.pattern.FrequencyScreen;
import edivad.dimstorage.container.ContainerDimTank;
import edivad.dimstorage.storage.DimTankStorage;
import edivad.dimstorage.tools.Translations;
import edivad.edivadlib.tools.utils.FluidUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ScreenDimTank extends FrequencyScreen<ContainerDimTank> {

  private static final ResourceLocation DIMTANK_GUI =
      new ResourceLocation(DimStorage.ID, "textures/gui/dimtank.png");

  public ScreenDimTank(ContainerDimTank container, Inventory inventory, Component text) {
    super(container, container.owner, inventory, text, DIMTANK_GUI, container.isOpen);
  }

  private static int getFluidScaled(int pixels, int currentLiquidAmount) {
    int maxLiquidAmount = DimTankStorage.CAPACITY;
    int x = currentLiquidAmount * pixels / maxLiquidAmount;
    return pixels - x;
  }

  @Override
  protected void init() {
    super.init();

    addComponent(new AutoEjectButton(width / 2 + 95, height / 2 + 75,
        (BlockEntityDimTank) blockEntityFrequencyOwner));

    drawSettings(drawSettings);
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);
    var liquidStack = ((BlockEntityDimTank) blockEntityFrequencyOwner).liquidState.clientLiquid;

    if (!liquidStack.isEmpty()) {
      var fluidType = liquidStack.getFluid().getFluidType();
      String liquidName = liquidStack.getDisplayName().getString();
      guiGraphics.drawString(this.font,
          Component.translatable(Translations.LIQUID,
              liquidName.substring(0, Math.min(14, liquidName.length()))),
          50, 25, 4210752, false);
      guiGraphics.drawString(this.font,
          Component.translatable(Translations.AMOUNT, liquidStack.getAmount()),
          50, 35, 4210752, false);
      guiGraphics.drawString(this.font,
          Component.translatable(Translations.TEMPERATURE, fluidType.getTemperature() - 273),
          50, 45, 4210752, false);
      guiGraphics.drawString(this.font,
          Component.translatable(Translations.LUMINOSITY, fluidType.getLightLevel()),
          50, 55, 4210752, false);
      guiGraphics.drawString(this.font,
          Component.translatable(Translations.GAS)
              .append(" ")
              .append(fluidType.isLighterThanAir()
                  ? Component.translatable(Translations.YES)
                  : Component.translatable(Translations.NO)),
          50, 65, 4210752, false);
    } else {
      guiGraphics.drawString(this.font,
          Component.translatable(Translations.LIQUID)
              .append(" ")
              .append(Component.translatable(Translations.EMPTY)),
          50, 25, 4210752, false);
    }
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);

    var fluid = ((BlockEntityDimTank) blockEntityFrequencyOwner).liquidState.clientLiquid;
    int z = getFluidScaled(60, fluid.getAmount());
    if (!fluid.isEmpty()) {
      var fluidTexture = FluidUtils.getFluidTexture(fluid);
      if (fluidTexture == null) {
        return;
      }

      var color = FluidUtils.getLiquidColorWithBiome(fluid, blockEntityFrequencyOwner);
      var red = FluidUtils.getRed(color);
      var green = FluidUtils.getGreen(color);
      var blue = FluidUtils.getBlue(color);
      var alpha = FluidUtils.getAlpha(color);

      guiGraphics.blit(this.leftPos + 11, this.topPos + 21 + z, 176, 16, 60 - z, fluidTexture,
          red, green, blue, alpha);
    }
  }
}
