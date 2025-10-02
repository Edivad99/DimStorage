package edivad.dimstorage.network.to_client;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentity.BlockEntityDimTank;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncLiquidTank(BlockPos pos, FluidStack fluidStack) implements CustomPacketPayload {

  public static final Type<SyncLiquidTank> TYPE =
      new Type<>(DimStorage.rl("sync_liquid_tank"));

  public static final StreamCodec<RegistryFriendlyByteBuf, SyncLiquidTank> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, SyncLiquidTank::pos,
          FluidStack.OPTIONAL_STREAM_CODEC, SyncLiquidTank::fluidStack,
          SyncLiquidTank::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SyncLiquidTank message, IPayloadContext ctx) {
    var level = ctx.player().level();
    if (level.isLoaded(message.pos)) {
      if (level.getBlockEntity(message.pos) instanceof BlockEntityDimTank tank) {
        tank.liquidState.sync(message.fluidStack);
      }
    }
  }
}
