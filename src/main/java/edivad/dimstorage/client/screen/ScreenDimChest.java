package edivad.dimstorage.client.screen;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.client.screen.pattern.FrequencyScreen;
import edivad.dimstorage.menu.DimChestMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ScreenDimChest extends FrequencyScreen<DimChestMenu> {

  private static final ResourceLocation DIMCHEST_GUI = DimStorage.rl("textures/gui/dimchest.png");

  public ScreenDimChest(DimChestMenu container, Inventory inventory, Component text) {
    super(container, container.owner, inventory, text, DIMCHEST_GUI, container.isOpen);
  }
}
