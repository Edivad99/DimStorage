package edivad.dimstorage.network.to_client;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.edivadlib.network.EdivadLibPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SyncLiquidTank(BlockPos pos, FluidStack fluidStack) implements EdivadLibPacket {

  public static final ResourceLocation ID = DimStorage.rl("sync_liquid_tank");

  public static SyncLiquidTank read(FriendlyByteBuf buf) {
    return new SyncLiquidTank(buf.readBlockPos(), buf.readFluidStack());
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(pos);
    buf.writeFluidStack(fluidStack);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext ctx) {
    ctx.level().ifPresent(level -> {
      if (level.isLoaded(pos)) {
        if (level.getBlockEntity(pos) instanceof BlockEntityDimTank tank) {
          tank.liquidState.sync(fluidStack);
        }
      }
    });
  }
}
