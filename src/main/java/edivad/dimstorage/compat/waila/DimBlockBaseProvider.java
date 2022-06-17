package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import net.minecraft.ChatFormatting;
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
import snownee.jade.api.ui.IElement;

public class DimBlockBaseProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {

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
        IElement title = tooltip.get(0, IElement.Align.LEFT).get(0);
        tooltip.clear();
        tooltip.add(title);
        if(accessor.getBlockEntity() instanceof BlockEntityFrequencyOwner) {
            CompoundTag data = accessor.getServerData();

            MutableComponent owner = Component.translatable("gui." + Main.MODID + ".owner").append(" ");
            MutableComponent freq = Component.translatable("gui." + Main.MODID + ".frequency").append(" ");
            MutableComponent locked = Component.translatable("gui." + Main.MODID + ".locked").append(" ");
            MutableComponent yes = Component.translatable("gui." + Main.MODID + ".yes");

            if(data.getBoolean(Main.MODID + ".HasOwner")) {
                ChatFormatting textColor = data.getBoolean(Main.MODID + ".CanAccess") ? ChatFormatting.GREEN : ChatFormatting.RED;
                tooltip.add(owner.append(data.getString(Main.MODID + ".Owner")).withStyle(textColor));
            }
            tooltip.add(freq.append(String.valueOf(data.getInt(Main.MODID + ".Frequency"))));
            if(data.getBoolean(Main.MODID + ".Locked"))
                tooltip.add(locked.append(yes));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Main.MODID, "dim_block_base");
    }
}