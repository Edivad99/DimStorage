package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.Main;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimChest;
import edivad.dimstorage.network.packet.UpdateDimTank;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tile.TileEntityDimTank;
import edivad.dimstorage.tile.TileFrequencyOwner;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;

public class LockButton extends Button {

    private final TileFrequencyOwner owner;

    public LockButton(int width, int height, TileFrequencyOwner owner) {
        super(width, height, 64, 20, getText(owner.locked), null);
        this.owner = owner;
    }

    private static TranslatableComponent getText(boolean isLock) {
        return new TranslatableComponent("gui." + Main.MODID + (isLock ? ".yes" : ".no"));
    }

    @Override
    public void onPress() {
        if(owner instanceof TileEntityDimChest chest) {
            chest.swapLocked();
            PacketHandler.INSTANCE.sendToServer(new UpdateDimChest(chest));
        }
        else if(owner instanceof TileEntityDimTank tank) {
            tank.swapLocked();
            PacketHandler.INSTANCE.sendToServer(new UpdateDimTank(tank));
        }
    }
}
