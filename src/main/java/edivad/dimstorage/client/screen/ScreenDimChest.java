package edivad.dimstorage.client.screen;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.client.screen.pattern.FrequencyScreen;
import edivad.dimstorage.container.ContainerDimChest;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ScreenDimChest extends FrequencyScreen<ContainerDimChest> {

  private static final ResourceLocation DIMCHEST_GUI =
      new ResourceLocation(DimStorage.ID, "textures/gui/dimchest.png");

  public ScreenDimChest(ContainerDimChest container, Inventory inventory, Component text) {
    super(container, container.owner, inventory, text, DIMCHEST_GUI, container.isOpen);
  }
}
