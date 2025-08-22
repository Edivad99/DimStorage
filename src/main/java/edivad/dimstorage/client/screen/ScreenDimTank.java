package edivad.dimstorage.client.screen;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.client.screen.element.button.AutoEjectButton;
import edivad.dimstorage.client.screen.pattern.FrequencyScreen;
import edivad.dimstorage.menu.DimTankMenu;
import edivad.dimstorage.storage.DimTankStorage;
import edivad.dimstorage.tools.Translations;
import edivad.edivadlib.tools.utils.FluidUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ScreenDimTank extends FrequencyScreen<DimTankMenu> {

  private static final ResourceLocation DIMTANK_GUI = DimStorage.rl("textures/gui/dimtank.png");

  public ScreenDimTank(DimTankMenu container, Inventory inventory, Component text) {
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
      String liquidName = liquidStack.getHoverName().getString();
      guiGraphics.drawString(this.font,
          Component.translatable(Translations.LIQUID,
              liquidName.substring(0, Math.min(14, liquidName.length()))),
          50, 25, 0xFF333333, false);
      guiGraphics.drawString(this.font,
          Component.translatable(Translations.AMOUNT, liquidStack.getAmount()),
          50, 35, 0xFF333333, false);
      guiGraphics.drawString(this.font,
          Component.translatable(Translations.TEMPERATURE, fluidType.getTemperature() - 273),
          50, 45, 0xFF333333, false);
      guiGraphics.drawString(this.font,
          Component.translatable(Translations.LUMINOSITY, fluidType.getLightLevel()),
          50, 55, 0xFF333333, false);
      guiGraphics.drawString(this.font,
          Component.translatable(Translations.GAS)
              .append(" ")
              .append(fluidType.isLighterThanAir()
                  ? Component.translatable(Translations.YES)
                  : Component.translatable(Translations.NO)),
          50, 65, 0xFF333333, false);
    } else {
      guiGraphics.drawString(this.font,
          Component.translatable(Translations.LIQUID, Component.translatable(Translations.EMPTY)),
          50, 25, 0xFF333333, false);
    }
  }

  @Override
  public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.renderContents(guiGraphics, mouseX, mouseY, partialTick);
    var fluid = ((BlockEntityDimTank) blockEntityFrequencyOwner).liquidState.clientLiquid;
    int z = getFluidScaled(60, fluid.getAmount());
    if (!fluid.isEmpty()) {
      var fluidTexture = FluidUtils.getFluidTexture(fluid);
      if (fluidTexture == null) {
        return;
      }

      var color = FluidUtils.getLiquidColorWithBiome(fluid, blockEntityFrequencyOwner);

      guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, fluidTexture, this.leftPos + 11,
          this.topPos + 21 + z, 16, 60 - z, color);
    }
  }
}
