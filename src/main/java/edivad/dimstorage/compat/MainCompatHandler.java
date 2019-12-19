package edivad.dimstorage.compat;

import java.util.Map;

import edivad.dimstorage.compat.top.TOPCompatibility;
import edivad.dimstorage.compat.waila.WailaCompatibility;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class MainCompatHandler {

	public static void registerTOP()
	{
		if(Loader.isModLoaded("theoneprobe"))
		{
			TOPCompatibility.register();
		}
	}
	public static void registerWaila()
	{
        if (Loader.isModLoaded("waila")) 
        {
            WailaCompatibility.register();
        }
    }
}
