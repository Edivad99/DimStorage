package edivad.dimstorage.setup;

import edivad.dimstorage.DimStorage;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DimStorageCreativeModeTabs {

  private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DimStorage.ID);

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DIMSTORAGE_TAB =
      CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
          .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
          .title(Component.literal(DimStorage.MODNAME))
          .icon(() -> new ItemStack(ModRegistration.DIMCHEST_ITEM.get()))
          .displayItems((parameters, output) -> {
            output.accept(new ItemStack(ModRegistration.DIMCHEST_ITEM.get()));
            output.accept(new ItemStack(ModRegistration.DIMTANK_ITEM.get()));
            output.accept(new ItemStack(ModRegistration.DIMTABLET.get()));
            output.accept(new ItemStack(ModRegistration.DIMCORE.get()));
            output.accept(new ItemStack(ModRegistration.DIMWALL.get()));
            output.accept(new ItemStack(ModRegistration.SOLIDDIMCORE.get()));
          }).build());

  public static void register(IEventBus modEventBus) {
    CREATIVE_MODE_TABS.register(modEventBus);
  }
}
