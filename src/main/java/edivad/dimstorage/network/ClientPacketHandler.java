package edivad.dimstorage.network;

import edivad.dimstorage.blockentities.BlockEntityDimTank;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

public class ClientPacketHandler {

    public static void syncronizeFluid(BlockPos pos, FluidStack fluidStack) {
        Level level = Minecraft.getInstance().level;
        if(level.isLoaded(pos)) {
            BlockEntity te = level.getBlockEntity(pos);
            if(te instanceof BlockEntityDimTank tank) {
                tank.liquidState.sync(fluidStack);
            }
        }
    }
}
