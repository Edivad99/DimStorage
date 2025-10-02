package edivad.dimstorage.client.screen.pattern;

import edivad.dimstorage.blockentity.BlockEntityDimChest;
import edivad.dimstorage.blockentity.BlockEntityDimTank;
import edivad.dimstorage.blockentity.BlockEntityFrequencyOwner;
import edivad.dimstorage.client.screen.element.button.ChangeButton;
import edivad.dimstorage.client.screen.element.button.LockButton;
import edivad.dimstorage.client.screen.element.button.OwnerButton;
import edivad.dimstorage.client.screen.element.textfield.FrequencyText;
import edivad.dimstorage.network.to_server.UpdateDimChest;
import edivad.dimstorage.network.to_server.UpdateDimTank;
import edivad.dimstorage.tools.Translations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public abstract class FrequencyScreen<T extends AbstractContainerMenu> extends PanelScreen<T> {

  private static final Component OWNER = Component.translatable(Translations.OWNER);
  private static final Component FREQ = Component.translatable(Translations.FREQUENCY);
  private static final Component LOCKED = Component.translatable(Translations.LOCKED);

  protected BlockEntityFrequencyOwner blockEntityFrequencyOwner;
  private FrequencyText freqTextField;

  public FrequencyScreen(T container, BlockEntityFrequencyOwner blockEntityFrequencyOwner,
      Inventory inventory, Component text, ResourceLocation background, boolean drawSettings) {
    super(container, inventory, text, background, drawSettings);
    this.blockEntityFrequencyOwner = blockEntityFrequencyOwner;
  }

  @Override
  protected void init() {
    super.init();

    clearComponent();
    addComponent(new OwnerButton(width / 2 + 95, height / 2 - 53, blockEntityFrequencyOwner));
    addComponent(new ChangeButton(width / 2 + 95, height / 2 + 7, b -> changeFrequency()));
    addComponent(new LockButton(width / 2 + 95, height / 2 + 46, blockEntityFrequencyOwner));

    freqTextField = new FrequencyText(width / 2 + 95, height / 2 - 12,
        blockEntityFrequencyOwner.getFrequency());
    addComponent(freqTextField);
    drawSettings(drawSettings);
  }

  private void changeFrequency() {
    int prevChannel = blockEntityFrequencyOwner.getFrequency().channel();
    try {
      int newFreq = Math.abs(Integer.parseInt(freqTextField.getValue()));
      blockEntityFrequencyOwner.setFrequency(
          blockEntityFrequencyOwner.getFrequency().setChannel(newFreq));

      if (blockEntityFrequencyOwner instanceof BlockEntityDimChest chest) {
        ClientPacketDistributor.sendToServer(new UpdateDimChest(chest));
      } else if (blockEntityFrequencyOwner instanceof BlockEntityDimTank tank) {
        ClientPacketDistributor.sendToServer(new UpdateDimTank(tank));
      }
    } catch (Exception e) {
      freqTextField.setValue(String.valueOf(prevChannel));
    }
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    super.render(guiGraphics, mouseX, mouseY, partialTicks);
    freqTextField.render(guiGraphics, mouseX, mouseY, partialTicks);
  }

  @Override
  public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
    freqTextField.mouseClicked(event, doubleClick);
    return super.mouseClicked(event, doubleClick);
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);

    if (drawSettings) {
      int posY = 45;

      // gameProfile
      guiGraphics.drawString(this.font, OWNER, 185, posY, 0xFF333333, false);
      posY += 9;
      guiGraphics.hLine(185, 185 + this.font.width(OWNER), posY, 0xFF333333);
      posY += 31;

      // freq
      guiGraphics.drawString(this.font, FREQ, 185, posY, 0xFF333333, false);
      posY += 9;
      guiGraphics.hLine(185, 185 + this.font.width(FREQ), posY, 0xFF333333);
      posY += 50;

      // locked
      guiGraphics.drawString(this.font, LOCKED, 185, posY, 0xFF333333, false);
      posY += 9;
      guiGraphics.hLine(185, 185 + this.font.width(LOCKED), posY, 0xFF333333);
    }
  }
}
