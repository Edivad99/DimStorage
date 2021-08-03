package edivad.dimstorage.setup.proxy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IProxy {

    Player getClientPlayer();

    Level getClientLevel();
}
