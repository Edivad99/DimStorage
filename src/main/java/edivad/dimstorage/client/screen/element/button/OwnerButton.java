package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimChest;
import edivad.dimstorage.network.packet.UpdateDimTank;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tile.TileEntityDimTank;
import edivad.dimstorage.tile.TileFrequencyOwner;
import edivad.dimstorage.tools.Config;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;

public class OwnerButton extends Button {

    private final TileFrequencyOwner owner;

    public OwnerButton(int width, int height, TileFrequencyOwner owner) {
        super(width, height, 64, 20, new TextComponent(owner.getFrequency().getOwner()), null);
        this.owner = owner;
        this.active = Config.DIMCHEST_ALLOWPRIVATENETWORK.get();
    }

    @Override
    public void onPress() {
        if(owner instanceof TileEntityDimChest chest) {
            chest.swapOwner();
            PacketHandler.INSTANCE.sendToServer(new UpdateDimChest(chest));
        }
        else if(owner instanceof TileEntityDimTank tank) {
            tank.swapOwner();
            PacketHandler.INSTANCE.sendToServer(new UpdateDimTank(tank));
        }
    }
}
