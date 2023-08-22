package edivad.dimstorage.network.packet;

import java.util.function.Supplier;
import edivad.dimstorage.network.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

public record SyncLiquidTank(BlockPos pos, FluidStack fluidStack) {

  public static SyncLiquidTank decode(FriendlyByteBuf buf) {
    return new SyncLiquidTank(buf.readBlockPos(), buf.readFluidStack());
  }

  public void encode(FriendlyByteBuf buf) {
    buf.writeBlockPos(pos);
    buf.writeFluidStack(fluidStack);
  }

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ClientPacketHandler.syncronizeFluid(pos, fluidStack);
    });
    ctx.get().setPacketHandled(true);
  }
}
