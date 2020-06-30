package edivad.dimstorage.client.screen;

import java.util.Collections;

import com.mojang.blaze3d.matrix.MatrixStack;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.screen.element.button.CollectButton;
import edivad.dimstorage.client.screen.pattern.FrequencyScreen;
import edivad.dimstorage.container.ContainerDimChest;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tools.Translate;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenDimChest extends FrequencyScreen<ContainerDimChest> {

    private TileEntityDimChest ownerTile;

    public ScreenDimChest(ContainerDimChest container, PlayerInventory invPlayer, ITextComponent text)
    {
        super(container, container.owner, invPlayer, text, new ResourceLocation(Main.MODID, "textures/gui/dimchest.png"), container.isOpen);
        this.ownerTile = container.owner;
    }

    @Override
    protected void func_231160_c_()//init
    {
        super.func_231160_c_();

        addComponent(new CollectButton(field_230708_k_ / 2 + 95, field_230709_l_ / 2 + 75, ownerTile));

        drawSettings(drawSettings);
    }

    @Override
    public void func_230430_a_(MatrixStack mStack, int mouseX, int mouseY, float partialTicks)//render
    {
        super.func_230430_a_(mStack, mouseX, mouseY, partialTicks);
        if(drawSettings && mouseX > field_230708_k_ / 2 + 90 && mouseX < field_230708_k_ / 2 + 164 && mouseY > field_230709_l_ / 2 + 70 && mouseY < field_230709_l_ / 2 + 100)
            this.func_238654_b_(mStack, Collections.singletonList(Translate.translateToLocal("tooltip." + Main.MODID + ".collect", TileEntityDimChest.AREA, TileEntityDimChest.AREA)), mouseX, mouseY, field_230712_o_);//renderToolTip font
    }
}
