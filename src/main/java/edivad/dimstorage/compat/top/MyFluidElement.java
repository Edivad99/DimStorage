//package edivad.dimstorage.compat.top;
//
//import edivad.dimstorage.DimStorage;
//import edivad.dimstorage.blockentity.BlockEntityDimTank;
//import edivad.edivadlib.compat.top.FluidElement;
//import edivad.edivadlib.tools.utils.FluidUtils;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.resources.Identifier;
//import net.neoforged.neoforge.fluids.FluidStack;
//
//public class MyFluidElement extends FluidElement {
//
//  public static final Identifier ID = DimStorage.id("fluid_element");
//
//  protected MyFluidElement(FluidStack fluid, int capacity, int colorLiquid) {
//    super(fluid, capacity, colorLiquid);
//  }
//
//  public MyFluidElement(BlockEntityDimTank blockentity, int capacity) {
//    super(blockentity.liquidState.serverLiquid, capacity,
//        FluidUtils.getLiquidColorWithBiome(blockentity.liquidState.serverLiquid, blockentity));
//  }
//
//  public MyFluidElement(RegistryFriendlyByteBuf buf) {
//    super(FluidStack.OPTIONAL_STREAM_CODEC.decode(buf), buf.readInt(), buf.readInt());
//  }
//
//  @Override
//  public Identifier getID() {
//    return ID;
//  }
//}
