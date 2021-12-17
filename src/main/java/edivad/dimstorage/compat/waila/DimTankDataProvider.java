package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DimTankDataProvider extends DimBlockBaseProvider implements IServerDataProvider<BlockEntity> {

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity tileEntity, boolean showDetails) {
        super.appendServerData(compoundTag, serverPlayer, level, tileEntity, showDetails);
        if(tileEntity instanceof BlockEntityDimTank blockentity) {
            compoundTag.putBoolean(Main.MODID + ".AutoEject", blockentity.autoEject);
            String liquidName = blockentity.liquidState.serverLiquid.getDisplayName().getString();
            int liquidLevel = blockentity.liquidState.serverLiquid.getAmount();
            compoundTag.putString(Main.MODID + ".Liquid", liquidName);
            compoundTag.putInt(Main.MODID + ".Amount", liquidLevel);
        }
    }
}