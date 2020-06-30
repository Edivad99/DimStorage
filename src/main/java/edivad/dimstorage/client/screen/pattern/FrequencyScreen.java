package edivad.dimstorage.client.screen.pattern;

import com.mojang.blaze3d.matrix.MatrixStack;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.screen.element.button.ChangeButton;
import edivad.dimstorage.client.screen.element.button.LockButton;
import edivad.dimstorage.client.screen.element.button.OwnerButton;
import edivad.dimstorage.client.screen.element.textfield.FrequencyText;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimChest;
import edivad.dimstorage.network.packet.UpdateDimTank;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tile.TileEntityDimTank;
import edivad.dimstorage.tile.TileFrequencyOwner;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class FrequencyScreen<T extends Container> extends PanelScreen<T> {

    protected TileFrequencyOwner tileOwner;

    private ITextProperties owner, freq, locked;
    private FrequencyText freqTextField;

    public FrequencyScreen(T container, TileFrequencyOwner tileOwner, PlayerInventory invPlayer, ITextComponent text, ResourceLocation background, boolean drawSettings)
    {
        super(container, invPlayer, text, background, drawSettings);
        this.tileOwner = tileOwner;
    }
    /*
    field_230708_k_ width 
    field_230709_l_ height 
    field_230690_l_ x 
    field_230691_m_ y 
    field_230685_b_ wasHovered 
    field_230692_n_ isHovered 
    field_230693_o_ active 
    field_230694_p_ visible
    func_238465_a_ hLine*/

    @Override
    protected void func_231160_c_()//init
    {
        super.func_231160_c_();

        // Get translation
        owner = new TranslationTextComponent("gui." + Main.MODID + ".owner");
        freq = new TranslationTextComponent("gui." + Main.MODID + ".frequency");
        locked = new TranslationTextComponent("gui." + Main.MODID + ".locked");

        clearComponent();
        addComponent(new OwnerButton(field_230708_k_ / 2 + 95, field_230709_l_ / 2 - 53, tileOwner));
        addComponent(new ChangeButton(field_230708_k_ / 2 + 95, field_230709_l_ / 2 + 7, b -> changeFrequency()));
        addComponent(new LockButton(field_230708_k_ / 2 + 95, field_230709_l_ / 2 + 46, tileOwner));

        freqTextField = new FrequencyText(field_230708_k_ / 2 + 95, field_230709_l_ / 2 - 12, tileOwner.frequency);
        addComponent(freqTextField);
        drawSettings(drawSettings);
    }

    private void changeFrequency()
    {
        int prevChannel = tileOwner.frequency.getChannel();
        try
        {
            int newFreq = Math.abs(Integer.parseInt(freqTextField.getText()));
            tileOwner.setFreq(tileOwner.frequency.copy().setChannel(newFreq));

            if(tileOwner instanceof TileEntityDimChest)
                PacketHandler.INSTANCE.sendToServer(new UpdateDimChest((TileEntityDimChest) tileOwner));
            else if(tileOwner instanceof TileEntityDimTank)
                PacketHandler.INSTANCE.sendToServer(new UpdateDimTank((TileEntityDimTank) tileOwner));
        }
        catch(Exception e)
        {
            freqTextField.setText(String.valueOf(prevChannel));
        }
    }

    @Override
    public void func_231023_e_()//tick
    {
        super.func_231023_e_();
        freqTextField.tick();
    }

    @Override
    public void func_230430_a_(MatrixStack mStack, int mouseX, int mouseY, float partialTicks)//render
    {
        super.func_230430_a_(mStack, mouseX, mouseY, partialTicks);
        freqTextField.func_230430_a_(mStack, mouseX, mouseY, partialTicks);//render
    }

    @Override
    public boolean func_231044_a_(double mouseX, double mouseY, int clickedButton)//mouseClicked
    {
        freqTextField.func_231044_a_(mouseX, mouseY, clickedButton);//mouseClicked
        return super.func_231044_a_(mouseX, mouseY, clickedButton);
    }

    @Override
    protected void func_230451_b_(MatrixStack mStack, int mouseX, int mouseY)//drawGuiContainerForegroundLayer
    {
        super.func_230451_b_(mStack, mouseX, mouseY);

        if(drawSettings)
        {
            int posY = 45;

            // owner
            this.field_230712_o_.func_238422_b_(mStack, owner, 185, posY, 4210752);//this.font.drawString
            posY += 9;
            this.func_238465_a_(mStack, 185, 185 + this.field_230712_o_.func_238414_a_(owner), posY, 0xFF333333);//hLine getStringWidth
            posY += 31;

            // freq
            this.field_230712_o_.func_238422_b_(mStack, freq, 185, posY, 4210752);//this.font.drawString
            posY += 9;
            this.func_238465_a_(mStack, 185, 185 + this.field_230712_o_.func_238414_a_(freq), posY, 0xFF333333);//hLine getStringWidth
            posY += 50;

            // locked
            this.field_230712_o_.func_238422_b_(mStack, locked, 185, posY, 4210752);//this.font.drawString
            posY += 9;
            this.func_238465_a_(mStack, 185, 185 + this.field_230712_o_.func_238414_a_(locked), posY, 0xFF333333);//hLine getStringWidth
        }
    }
}
