package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.blockentities.BlockEntityDimChest;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import edivad.dimstorage.network.to_server.UpdateDimChest;
import edivad.dimstorage.network.to_server.UpdateDimTank;
import edivad.dimstorage.tools.Translations;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

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
  public void onPress() {
    owner.swapLocked();
    if (owner instanceof BlockEntityDimChest chest) {
      PacketDistributor.sendToServer(new UpdateDimChest(chest));
    } else if (owner instanceof BlockEntityDimTank tank) {
      PacketDistributor.sendToServer(new UpdateDimTank(tank));
    }
  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    defaultButtonNarrationText(narrationElementOutput);
  }
}
