package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.blockentity.BlockEntityDimChest;
import edivad.dimstorage.blockentity.BlockEntityDimTank;
import edivad.dimstorage.blockentity.BlockEntityFrequencyOwner;
import edivad.dimstorage.network.to_server.UpdateDimChest;
import edivad.dimstorage.network.to_server.UpdateDimTank;
import edivad.dimstorage.setup.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class OwnerButton extends AbstractButton {

  private final BlockEntityFrequencyOwner owner;

  public OwnerButton(int width, int height, BlockEntityFrequencyOwner owner) {
    super(width, height, 64, 20, getText(owner));
    this.owner = owner;
    this.active = Config.DimBlock.ALLOW_PRIVATE_NETWORK.get();
  }

  private static Component getText(BlockEntityFrequencyOwner owner) {
    return Component.literal(owner.getFrequency().getOwner());
  }

  @Override
  protected void renderContents(GuiGraphics guiGraphics, int i, int i1, float v) {
    this.renderDefaultSprite(guiGraphics);
    this.renderDefaultLabel(guiGraphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE));
  }

  @Override
  public void onPress(InputWithModifiers inputWithModifiers) {
    owner.swapOwner(Minecraft.getInstance().player);
    if (owner instanceof BlockEntityDimChest chest) {
      ClientPacketDistributor.sendToServer(new UpdateDimChest(chest));
    } else if (owner instanceof BlockEntityDimTank tank) {
      ClientPacketDistributor.sendToServer(new UpdateDimTank(tank));
    }
  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    defaultButtonNarrationText(narrationElementOutput);
  }
}
