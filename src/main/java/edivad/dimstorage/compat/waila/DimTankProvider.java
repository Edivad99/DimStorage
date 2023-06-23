package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;

public class DimTankProvider extends DimBlockBaseProvider {

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor accessor) {
        BlockEntityDimTank blockEntity = (BlockEntityDimTank) accessor.getBlockEntity();
        compoundTag.putBoolean(Main.MODID + ".AutoEject", blockEntity.autoEject);
        String liquidName = blockEntity.liquidState.serverLiquid.getFluid().getFluidType().getDescriptionId();
        int liquidLevel = blockEntity.liquidState.serverLiquid.getAmount();
        compoundTag.putString(Main.MODID + ".Liquid", liquidName);
        compoundTag.putInt(Main.MODID + ".Amount", liquidLevel);
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Main.MODID, "dim_tank");
    }
}
