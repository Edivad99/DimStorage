package edivad.dimstorage.network.packet;

import java.util.function.Supplier;

import edivad.dimstorage.Main;
import edivad.dimstorage.tile.TileEntityDimTank;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public class SyncLiquidTank {

    private BlockPos pos;
    private FluidStack fluidStack;

    public SyncLiquidTank(FriendlyByteBuf buf)
    {
        pos = buf.readBlockPos();
        fluidStack = buf.readFluidStack();
    }

    public SyncLiquidTank(BlockPos pos, FluidStack fluidStack)
    {
        this.pos = pos;
        this.fluidStack = fluidStack;
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeBlockPos(pos);
        buf.writeFluidStack(fluidStack);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            Level world = Main.proxy.getClientWorld();
            if(world.isLoaded(pos))
            {
                BlockEntity te = world.getBlockEntity(pos);
                if(te instanceof TileEntityDimTank)
                {
                    TileEntityDimTank tank = (TileEntityDimTank) te;
                    tank.liquidState.sync(fluidStack);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
