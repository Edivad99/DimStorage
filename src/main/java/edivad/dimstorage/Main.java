package edivad.dimstorage;

import com.mojang.logging.LogUtils;
import edivad.dimstorage.setup.ClientSetup;
import edivad.dimstorage.setup.Config;
import edivad.dimstorage.setup.ModSetup;
import edivad.dimstorage.setup.Registration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Main.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Main {

    public static final String MODID = "dimstorage";
    public static final String MODNAME = "DimStorage";

    public static final Logger LOGGER = LogUtils.getLogger();

    public Main() {
        Registration.init();
        Config.init();

        // Register the setup method for modloading
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ModSetup::init);
        modEventBus.addListener(ClientSetup::init);
        modEventBus.addListener(Main::onCreativeModeTabRegister);
    }

    private static void onCreativeModeTabRegister(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MODID, "tab"),
                builder -> builder
                        .icon(() -> new ItemStack(Registration.DIMCHEST_ITEM.get()))
                        .title(Component.literal(MODNAME))
                        .displayItems((features, output, hasPermissions) -> {
                            output.accept(new ItemStack(Registration.DIMCHEST_ITEM.get()));
                            output.accept(new ItemStack(Registration.DIMTANK_ITEM.get()));
                            output.accept(new ItemStack(Registration.DIMTABLET.get()));
                            output.accept(new ItemStack(Registration.DIMCORE.get()));
                            output.accept(new ItemStack(Registration.DIMWALL.get()));
                            output.accept(new ItemStack(Registration.SOLIDDIMCORE.get()));
                        }));
    }
}
