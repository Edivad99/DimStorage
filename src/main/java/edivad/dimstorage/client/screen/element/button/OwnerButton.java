package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.blockentities.BlockEntityDimChest;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.to_server.UpdateDimChest;
import edivad.dimstorage.network.to_server.UpdateDimTank;
import edivad.dimstorage.setup.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

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
  public void onPress() {
    owner.swapOwner(Minecraft.getInstance().player);
    if (owner instanceof BlockEntityDimChest chest) {
      PacketHandler.sendToServer(new UpdateDimChest(chest));
    } else if (owner instanceof BlockEntityDimTank tank) {
      PacketHandler.sendToServer(new UpdateDimTank(tank));
    }
  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    defaultButtonNarrationText(narrationElementOutput);
  }
}
