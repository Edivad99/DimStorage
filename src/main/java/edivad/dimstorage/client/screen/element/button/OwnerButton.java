package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.blockentities.BlockEntityDimChest;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimChest;
import edivad.dimstorage.network.packet.UpdateDimTank;
import edivad.dimstorage.setup.Config;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class OwnerButton extends AbstractButton {

    private final BlockEntityFrequencyOwner owner;

    public OwnerButton(int width, int height, BlockEntityFrequencyOwner owner) {
        super(width, height, 64, 20, Component.literal(owner.getFrequency().getOwner()));
        this.owner = owner;
        this.active = Config.DimBlock.ALLOW_PRIVATE_NETWORK.get();
    }

    @Override
    public void onPress() {
        owner.swapOwner();
        if(owner instanceof BlockEntityDimChest chest) {
            PacketHandler.INSTANCE.sendToServer(new UpdateDimChest(chest));
        } else if(owner instanceof BlockEntityDimTank tank) {
            PacketHandler.INSTANCE.sendToServer(new UpdateDimTank(tank));
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        defaultButtonNarrationText(narrationElementOutput);
    }
}
