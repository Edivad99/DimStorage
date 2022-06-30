package edivad.dimstorage.compat.top;

import edivad.dimstorage.Main;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.edivadlib.compat.top.FluidElement;
import edivad.edivadlib.tools.utils.FluidUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class MyFluidElement extends FluidElement {

    public static final ResourceLocation ID = new ResourceLocation(Main.MODID, "fluid_element");

    protected MyFluidElement(FluidStack fluid, int capacity, int colorLiquid) {
        super(fluid, capacity, colorLiquid);
    }

    public MyFluidElement(BlockEntityDimTank blockentity, int capacity) {
        super(blockentity.liquidState.serverLiquid, capacity, FluidUtils.getLiquidColorWithBiome(blockentity.liquidState.serverLiquid, blockentity));
    }

    public MyFluidElement(FriendlyByteBuf buf) {
        super(buf.readFluidStack(), buf.readInt(), buf.readInt());
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
