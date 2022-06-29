package edivad.dimstorage.compat.waila;

import edivad.dimstorage.Main;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import edivad.dimstorage.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;

public class DimBlockBaseComponent implements IBlockComponentProvider {

    private static final MutableComponent OWNER = Component.translatable(Translations.OWNER);
    private static final MutableComponent FREQ = Component.translatable(Translations.FREQUENCY);
    private static final MutableComponent LOCKED = Component.translatable(Translations.LOCKED);
    private static final MutableComponent YES = Component.translatable(Translations.YES);

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        //Remove extra info from Waila
        IElement title = tooltip.get(0, IElement.Align.LEFT).get(0);
        tooltip.clear();
        tooltip.add(title);
        if(accessor.getBlockEntity() instanceof BlockEntityFrequencyOwner) {
            CompoundTag data = accessor.getServerData();

            if(data.getBoolean(Main.MODID + ".HasOwner")) {
                ChatFormatting textColor = data.getBoolean(Main.MODID + ".CanAccess") ? ChatFormatting.GREEN : ChatFormatting.RED;
                tooltip.add(OWNER.copy().append(" " + data.getString(Main.MODID + ".Owner")).withStyle(textColor));
            }
            tooltip.add(FREQ.copy().append(" " + data.getInt(Main.MODID + ".Frequency")));
            if(data.getBoolean(Main.MODID + ".Locked"))
                tooltip.add(LOCKED.copy().append(" ").append(YES));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Main.MODID, "dim_block_base");
    }
}
