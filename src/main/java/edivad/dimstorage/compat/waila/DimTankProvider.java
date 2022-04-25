package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DimTankProvider extends DimBlockBaseProvider implements IComponentProvider {

    public static DimTankProvider INSTANCE = new DimTankProvider();

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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        super.appendTooltip(tooltip, accessor, config);
        if(accessor.getBlockEntity() instanceof BlockEntityDimTank) {
            CompoundTag data = accessor.getServerData();

            String autoEject = new TranslatableComponent("gui." + Main.MODID + ".eject").getString() + ": ";
            String yes = new TranslatableComponent("gui." + Main.MODID + ".yes").getString();
            String liquid = new TranslatableComponent("gui." + Main.MODID + ".liquid").getString();
            String amount = new TranslatableComponent("gui." + Main.MODID + ".amount").getString();

            if(data.getBoolean(Main.MODID + ".AutoEject"))
                tooltip.add(new TextComponent(autoEject + yes));
            if(data.getInt(Main.MODID + ".Amount") > 0) {
                tooltip.add(new TextComponent(liquid + " " + data.getString(Main.MODID + ".Liquid")));
                tooltip.add(new TextComponent(amount + " " + data.getInt(Main.MODID + ".Amount") + " mB"));
            }

        }
    }
}