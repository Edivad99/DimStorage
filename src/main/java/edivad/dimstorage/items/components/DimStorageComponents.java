package edivad.dimstorage.items.components;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.api.Frequency;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DimStorageComponents {

  private static final DeferredRegister.DataComponents deferredRegister =
      DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, DimStorage.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Frequency>> FREQUENCY =
      deferredRegister.registerComponentType("frequency", builder ->
          builder
              .persistent(Frequency.CODEC)
              .networkSynchronized(Frequency.STREAM_CODEC));

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<FrequencyTabletComponent>> FREQUENCY_TABLET =
      deferredRegister.registerComponentType("frequency_tablet", builder ->
          builder
              .persistent(FrequencyTabletComponent.CODEC)
              .networkSynchronized(FrequencyTabletComponent.STREAM_CODEC));
}
