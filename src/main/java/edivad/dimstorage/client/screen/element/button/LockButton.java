package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.blockentities.BlockEntityDimChest;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimChest;
import edivad.dimstorage.network.packet.UpdateDimTank;
import edivad.dimstorage.tools.Translations;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class LockButton extends Button {

    private final BlockEntityFrequencyOwner owner;

    public LockButton(int width, int height, BlockEntityFrequencyOwner owner) {
        super(width, height, 64, 20, getText(owner.locked), null);
        this.owner = owner;
    }

    private static Component getText(boolean isLock) {
        return Component.translatable(isLock ? Translations.YES : Translations.NO);
    }

    @Override
    public void onPress() {
        owner.swapLocked();
        if(owner instanceof BlockEntityDimChest chest) {
            PacketHandler.INSTANCE.sendToServer(new UpdateDimChest(chest));
        } else if(owner instanceof BlockEntityDimTank tank) {
            PacketHandler.INSTANCE.sendToServer(new UpdateDimTank(tank));
        }
    }
}
