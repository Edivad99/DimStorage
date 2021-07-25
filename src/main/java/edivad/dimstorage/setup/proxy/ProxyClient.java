package edivad.dimstorage.setup.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ProxyClient implements IProxy {

    @Override
    public Player getClientPlayer()
    {
        return Minecraft.getInstance().player;
    }

    @Override
    public Level getClientWorld()
    {
        return Minecraft.getInstance().level;
    }
}
