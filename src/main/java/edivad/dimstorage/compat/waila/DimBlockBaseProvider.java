package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DimBlockBaseProvider implements IComponentProvider, IServerDataProvider<BlockEntity> {

    public static DimBlockBaseProvider INSTANCE = new DimBlockBaseProvider();

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity tileEntity, boolean showDetails) {
        if(tileEntity instanceof BlockEntityFrequencyOwner blockentity) {
            Frequency frequency = blockentity.getFrequency();
            compoundTag.putBoolean(Main.MODID + ".HasOwner", frequency.hasOwner());
            compoundTag.putBoolean(Main.MODID + ".CanAccess", blockentity.canAccess(serverPlayer));
            compoundTag.putString(Main.MODID + ".Owner", frequency.getOwner());
            compoundTag.putInt(Main.MODID + ".Frequency", frequency.getChannel());
            compoundTag.putBoolean(Main.MODID + ".Locked", blockentity.locked);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        //Remove extra info from Waila
        tooltip.clear();
        if(accessor.getBlockEntity() instanceof BlockEntityFrequencyOwner) {
            CompoundTag data = accessor.getServerData();

            String owner = new TranslatableComponent("gui." + Main.MODID + ".owner").getString() + " ";
            String freq = new TranslatableComponent("gui." + Main.MODID + ".frequency").getString() + " ";
            String locked = new TranslatableComponent("gui." + Main.MODID + ".locked").getString() + " ";
            String yes = new TranslatableComponent("gui." + Main.MODID + ".yes").getString();

            if(data.getBoolean(Main.MODID + ".HasOwner")) {
                if(data.getBoolean(Main.MODID + ".CanAccess"))
                    tooltip.add(new TextComponent(ChatFormatting.GREEN + owner + data.getString(Main.MODID + ".Owner")));
                else
                    tooltip.add(new TextComponent(ChatFormatting.RED + owner + data.getString(Main.MODID + ".Owner")));
            }
            tooltip.add(new TextComponent(freq + data.getInt(Main.MODID + ".Frequency")));
            if(data.getBoolean(Main.MODID + ".Locked"))
                tooltip.add(new TextComponent(locked + yes));
        }
    }
}