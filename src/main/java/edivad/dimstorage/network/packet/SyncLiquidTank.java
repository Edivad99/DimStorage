package edivad.dimstorage.network.packet;

import edivad.dimstorage.network.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncLiquidTank {

    private BlockPos pos;
    private FluidStack fluidStack;

    public SyncLiquidTank(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        fluidStack = buf.readFluidStack();
    }

    public SyncLiquidTank(BlockPos pos, FluidStack fluidStack) {
        this.pos = pos;
        this.fluidStack = fluidStack;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeFluidStack(fluidStack);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.syncronizeFluid(pos, fluidStack)));
        ctx.get().setPacketHandled(true);
    }

}
