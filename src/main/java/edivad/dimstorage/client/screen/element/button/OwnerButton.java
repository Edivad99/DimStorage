package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimChest;
import edivad.dimstorage.network.packet.UpdateDimTank;
import edivad.dimstorage.blockentities.BlockEntityDimChest;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import edivad.dimstorage.tools.Config;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;

public class OwnerButton extends Button {

    private final BlockEntityFrequencyOwner owner;

    public OwnerButton(int width, int height, BlockEntityFrequencyOwner owner) {
        super(width, height, 64, 20, new TextComponent(owner.getFrequency().getOwner()), null);
        this.owner = owner;
        this.active = Config.DIMCHEST_ALLOWPRIVATENETWORK.get();
    }

    @Override
    public void onPress() {
        if(owner instanceof BlockEntityDimChest chest) {
            chest.swapOwner();
            PacketHandler.INSTANCE.sendToServer(new UpdateDimChest(chest));
        }
        else if(owner instanceof BlockEntityDimTank tank) {
            tank.swapOwner();
            PacketHandler.INSTANCE.sendToServer(new UpdateDimTank(tank));
        }
    }
}
