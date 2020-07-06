package edivad.dimstorage.compat;

import edivad.dimstorage.compat.top.TOPProvider;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;

public class MainCompatHandler {

    public static void registerTOP()
    {
        if(ModList.get().isLoaded("theoneprobe"))
        {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPProvider::new);
        }
    }
}
