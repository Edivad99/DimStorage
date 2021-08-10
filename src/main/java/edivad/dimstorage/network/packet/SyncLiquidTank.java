package edivad.dimstorage.network.packet;

import edivad.dimstorage.Main;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

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
        ctx.get().enqueueWork(() -> {
            Level level = Main.proxy.getClientLevel();
            if(level.isLoaded(pos)) {
                BlockEntity te = level.getBlockEntity(pos);
                if(te instanceof BlockEntityDimTank tank) {
                    tank.liquidState.sync(fluidStack);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
