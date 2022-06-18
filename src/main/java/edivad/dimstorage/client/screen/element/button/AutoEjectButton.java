package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimTank;
import edivad.dimstorage.tools.Translations;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class AutoEjectButton extends Button {

    private final BlockEntityDimTank tank;

    public AutoEjectButton(int width, int height, BlockEntityDimTank tank) {
        super(width, height, 64, 20, getText(tank.autoEject), null);
        this.tank = tank;
    }

    private static Component getText(boolean autoEject) {
        return Component.translatable(autoEject ? Translations.EJECT : Translations.IDLE);
    }

    @Override
    public void onPress() {
        tank.swapAutoEject();
        PacketHandler.INSTANCE.sendToServer(new UpdateDimTank(tank));
    }
}
