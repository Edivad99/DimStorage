package edivad.dimstorage.client.screen;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.screen.pattern.FrequencyScreen;
import edivad.dimstorage.container.ContainerDimChest;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenDimChest extends FrequencyScreen<ContainerDimChest> {

    public ScreenDimChest(ContainerDimChest container, PlayerInventory invPlayer, ITextComponent text)
    {
        super(container, container.owner, invPlayer, text, new ResourceLocation(Main.MODID, "textures/gui/dimchest.png"), container.isOpen);
    }
}
