package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.blockentity.BlockEntityDimChest;
import edivad.dimstorage.blockentity.BlockEntityDimTank;
import edivad.dimstorage.blockentity.BlockEntityFrequencyOwner;
import edivad.dimstorage.network.to_server.UpdateDimChest;
import edivad.dimstorage.network.to_server.UpdateDimTank;
import edivad.dimstorage.tools.Translations;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class LockButton extends AbstractButton {

  private final BlockEntityFrequencyOwner owner;

  public LockButton(int width, int height, BlockEntityFrequencyOwner owner) {
    super(width, height, 64, 20, getText(owner.isLocked()));
    this.owner = owner;
  }

  private static Component getText(boolean isLock) {
    return Component.translatable(isLock ? Translations.YES : Translations.NO);
  }

  @Override
  protected void extractContents(GuiGraphicsExtractor guiGraphicsExtractor, int i, int i1,
      float v) {
    this.extractDefaultSprite(guiGraphicsExtractor);
    this.extractDefaultLabel(guiGraphicsExtractor.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE));
  }

  @Override
  public void onPress(InputWithModifiers inputWithModifiers) {
    owner.swapLocked();
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
