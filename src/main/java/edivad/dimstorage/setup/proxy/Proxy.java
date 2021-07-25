package edivad.dimstorage.setup.proxy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Proxy implements IProxy {

    @Override
    public Player getClientPlayer()
    {
        throw new IllegalStateException("This should only be called from client side");
    }

    @Override
    public Level getClientWorld()
    {
        throw new IllegalStateException("This should only be called from client side");
    }
}
