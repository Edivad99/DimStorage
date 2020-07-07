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

    @Override
    protected void init()
    {
        super.init();

        // Get translation
        owner = new TranslationTextComponent("gui." + Main.MODID + ".owner");
        freq = new TranslationTextComponent("gui." + Main.MODID + ".frequency");
        locked = new TranslationTextComponent("gui." + Main.MODID + ".locked");

        clearComponent();
        addComponent(new OwnerButton(width / 2 + 95, height / 2 - 53, tileOwner));
        addComponent(new ChangeButton(width / 2 + 95, height / 2 + 7, b -> changeFrequency()));
        addComponent(new LockButton(width / 2 + 95, height / 2 + 46, tileOwner));

        freqTextField = new FrequencyText(width / 2 + 95, height / 2 - 12, tileOwner.frequency);
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
    public void tick()
    {
        super.tick();
        freqTextField.tick();
    }

    @Override
    public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(mStack, mouseX, mouseY, partialTicks);
        freqTextField.render(mStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickedButton)
    {
        freqTextField.mouseClicked(mouseX, mouseY, clickedButton);
        return super.mouseClicked(mouseX, mouseY, clickedButton);
    }

    @Override
    protected void func_230451_b_(MatrixStack mStack, int mouseX, int mouseY)//drawGuiContainerForegroundLayer
    {
        super.func_230451_b_(mStack, mouseX, mouseY);

        if(drawSettings)
        {
            int posY = 45;

            // owner
            this.font.func_238422_b_(mStack, owner, 185, posY, 4210752);//this.font.drawString
            posY += 9;
            this.hLine(mStack, 185, 185 + this.font.func_238414_a_(owner), posY, 0xFF333333);//getStringWidth
            posY += 31;

            // freq
            this.font.func_238422_b_(mStack, freq, 185, posY, 4210752);//this.font.drawString
            posY += 9;
            this.hLine(mStack, 185, 185 + this.font.func_238414_a_(freq), posY, 0xFF333333);//getStringWidth
            posY += 50;

            // locked
            this.font.func_238422_b_(mStack, locked, 185, posY, 4210752);//this.font.drawString
            posY += 9;
            this.hLine(mStack, 185, 185 + this.font.func_238414_a_(locked), posY, 0xFF333333);//getStringWidth
        }
    }
}
