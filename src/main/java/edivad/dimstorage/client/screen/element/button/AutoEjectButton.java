package edivad.dimstorage.client.screen.element.button;

import edivad.dimstorage.Main;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateDimTank;
import edivad.dimstorage.tile.TileEntityDimTank;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;

public class AutoEjectButton extends Button {

    private final TileEntityDimTank tank;

    public AutoEjectButton(int width, int height, TileEntityDimTank tank) {
        super(width, height, 64, 20, getText(tank.autoEject), null);
        this.tank = tank;
    }

    private static TranslatableComponent getText(boolean autoEject) {
        return new TranslatableComponent("gui." + Main.MODID + (autoEject ? ".eject" : ".idle"));
    }

    @Override
    public void onPress() {
        tank.swapAutoEject();
        PacketHandler.INSTANCE.sendToServer(new UpdateDimTank(tank));
    }
}
