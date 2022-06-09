package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.Main;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimTank;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class AutoEjectButton extends Button {

    private final BlockEntityDimTank tank;

    public AutoEjectButton(int width, int height, BlockEntityDimTank tank) {
        super(width, height, 64, 20, getText(tank.autoEject), null);
        this.tank = tank;
    }

    private static Component getText(boolean autoEject) {
        return Component.translatable("gui." + Main.MODID + (autoEject ? ".eject" : ".idle"));
    }

    @Override
    public void onPress() {
        tank.swapAutoEject();
        PacketHandler.INSTANCE.sendToServer(new UpdateDimTank(tank));
    }
}
