package edivad.dimstorage.client.screen.pattern;

import com.mojang.blaze3d.vertex.PoseStack;
import edivad.dimstorage.Main;
import edivad.dimstorage.client.screen.element.button.ChangeButton;
import edivad.dimstorage.client.screen.element.button.LockButton;
import edivad.dimstorage.client.screen.element.button.OwnerButton;
import edivad.dimstorage.client.screen.element.textfield.FrequencyText;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimChest;
import edivad.dimstorage.network.packet.UpdateDimTank;
import edivad.dimstorage.blockentities.BlockEntityDimChest;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class FrequencyScreen<T extends AbstractContainerMenu> extends PanelScreen<T> {

    protected BlockEntityFrequencyOwner tileOwner;

    private Component owner, freq, locked;
    private FrequencyText freqTextField;

    public FrequencyScreen(T container, BlockEntityFrequencyOwner tileOwner, Inventory inventory, Component text, ResourceLocation background, boolean drawSettings) {
        super(container, inventory, text, background, drawSettings);
        this.tileOwner = tileOwner;
    }

    @Override
    protected void init() {
        super.init();

        // Get translation
        owner = new TranslatableComponent("gui." + Main.MODID + ".owner");
        freq = new TranslatableComponent("gui." + Main.MODID + ".frequency");
        locked = new TranslatableComponent("gui." + Main.MODID + ".locked");

        clearComponent();
        addComponent(new OwnerButton(width / 2 + 95, height / 2 - 53, tileOwner));
        addComponent(new ChangeButton(width / 2 + 95, height / 2 + 7, b -> changeFrequency()));
        addComponent(new LockButton(width / 2 + 95, height / 2 + 46, tileOwner));

        freqTextField = new FrequencyText(width / 2 + 95, height / 2 - 12, tileOwner.getFrequency());
        addComponent(freqTextField);
        drawSettings(drawSettings);
    }

    private void changeFrequency() {
        int prevChannel = tileOwner.getFrequency().getChannel();
        try {
            int newFreq = Math.abs(Integer.parseInt(freqTextField.getValue()));
            tileOwner.setFrequency(tileOwner.getFrequency().setChannel(newFreq));

            if(tileOwner instanceof BlockEntityDimChest)
                PacketHandler.INSTANCE.sendToServer(new UpdateDimChest((BlockEntityDimChest) tileOwner));
            else if(tileOwner instanceof BlockEntityDimTank)
                PacketHandler.INSTANCE.sendToServer(new UpdateDimTank((BlockEntityDimTank) tileOwner));
        }
        catch(Exception e) {
            freqTextField.setValue(String.valueOf(prevChannel));
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        freqTextField.tick();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);
        freqTextField.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickedButton) {
        freqTextField.mouseClicked(mouseX, mouseY, clickedButton);
        return super.mouseClicked(mouseX, mouseY, clickedButton);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        super.renderLabels(poseStack, mouseX, mouseY);

        if(drawSettings) {
            int posY = 45;

            // owner
            this.font.draw(poseStack, owner, 185, posY, 4210752);
            posY += 9;
            this.hLine(poseStack, 185, 185 + this.font.width(owner), posY, 0xFF333333);
            posY += 31;

            // freq
            this.font.draw(poseStack, freq, 185, posY, 4210752);
            posY += 9;
            this.hLine(poseStack, 185, 185 + this.font.width(freq), posY, 0xFF333333);
            posY += 50;

            // locked
            this.font.draw(poseStack, locked, 185, posY, 4210752);
            posY += 9;
            this.hLine(poseStack, 185, 185 + this.font.width(locked), posY, 0xFF333333);
        }
    }
}
