package edivad.dimstorage.setup;

import edivad.dimstorage.DimStorage;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DimStorageCreativeModeTabs {

  private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DimStorage.ID);

  public static final RegistryObject<CreativeModeTab> DIMSTORAGE_TAB =
      CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
          .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
          .title(Component.literal(DimStorage.MODNAME))
          .icon(() -> new ItemStack(Registration.DIMCHEST_ITEM.get()))
          .displayItems((parameters, output) -> {
            output.accept(new ItemStack(Registration.DIMCHEST_ITEM.get()));
            output.accept(new ItemStack(Registration.DIMTANK_ITEM.get()));
            output.accept(new ItemStack(Registration.DIMTABLET.get()));
            output.accept(new ItemStack(Registration.DIMCORE.get()));
            output.accept(new ItemStack(Registration.DIMWALL.get()));
            output.accept(new ItemStack(Registration.SOLIDDIMCORE.get()));
          }).build());

  public static void register(IEventBus modEventBus) {
    CREATIVE_MODE_TABS.register(modEventBus);
  }
}
