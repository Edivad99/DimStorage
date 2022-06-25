package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.tools.Translations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class DimTankProvider extends DimBlockBaseProvider implements IServerDataProvider<BlockEntity> {

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity tileEntity, boolean showDetails) {
        super.appendServerData(compoundTag, serverPlayer, level, tileEntity, showDetails);
        if(tileEntity instanceof BlockEntityDimTank blockentity) {
            compoundTag.putBoolean(Main.MODID + ".AutoEject", blockentity.autoEject);
            String liquidName = blockentity.liquidState.serverLiquid.getFluid().getFluidType().getDescriptionId();
            int liquidLevel = blockentity.liquidState.serverLiquid.getAmount();
            compoundTag.putString(Main.MODID + ".Liquid", liquidName);
            compoundTag.putInt(Main.MODID + ".Amount", liquidLevel);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Main.MODID, "dim_tank");
    }
}