package edivad.dimstorage.client.screen;

import java.util.Collections;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.screen.element.button.CollectButton;
import edivad.dimstorage.client.screen.pattern.FrequencyScreen;
import edivad.dimstorage.container.ContainerDimChest;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ScreenDimChest extends FrequencyScreen<ContainerDimChest> {

    private TileEntityDimChest ownerTile;

    public ScreenDimChest(ContainerDimChest container, PlayerInventory invPlayer, ITextComponent text)
    {
        super(container, container.owner, invPlayer, text, new ResourceLocation(Main.MODID, "textures/gui/dimchest.png"), container.isOpen);
        this.ownerTile = container.owner;
    }

    @Override
    protected void init()
    {
        super.init();

        addComponent(new CollectButton(width / 2 + 95, height / 2 + 75, ownerTile));

        drawSettings(drawSettings);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        super.render(mouseX, mouseY, partialTicks);
        if(drawSettings && mouseX > this.width / 2 + 90 && mouseX < this.width / 2 + 164 && mouseY > this.height / 2 + 70 && mouseY < this.height / 2 + 100)
            this.renderTooltip(Collections.singletonList(new TranslationTextComponent("tooltip." + Main.MODID + ".collect", TileEntityDimChest.AREA, TileEntityDimChest.AREA).getString()), mouseX, mouseY, font);
    }
}
