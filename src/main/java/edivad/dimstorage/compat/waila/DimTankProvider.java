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

public class DimTankProvider extends DimBlockBaseProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {

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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        super.appendTooltip(tooltip, accessor, config);
        if(accessor.getBlockEntity() instanceof BlockEntityDimTank) {
            CompoundTag data = accessor.getServerData();

            MutableComponent autoEject = Component.translatable(Translations.EJECT).append(": ");
            MutableComponent yes = Component.translatable(Translations.YES);
            MutableComponent liquid = Component.translatable(Translations.LIQUID).append(" ");
            MutableComponent amount = Component.translatable(Translations.AMOUNT).append(" ");

            if(data.getBoolean(Main.MODID + ".AutoEject"))
                tooltip.add(autoEject.append(yes));
            if(data.getInt(Main.MODID + ".Amount") > 0) {
                tooltip.add(liquid.append(Component.translatable(data.getString(Main.MODID + ".Liquid")).getString()));
                tooltip.add(amount.append(String.valueOf(data.getInt(Main.MODID + ".Amount"))).append(" mB"));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Main.MODID, "dim_tank");
    }
}