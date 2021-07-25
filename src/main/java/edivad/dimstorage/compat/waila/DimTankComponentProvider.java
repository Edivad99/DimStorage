//package edivad.dimstorage.compat.waila;
//
//import java.util.List;
//
//import edivad.dimstorage.Main;
//import edivad.dimstorage.tile.TileEntityDimTank;
//import mcp.mobius.waila.api.IComponentProvider;
//import mcp.mobius.waila.api.IDataAccessor;
//import mcp.mobius.waila.api.IPluginConfig;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraft.util.text.TranslationTextComponent;
//
//public class DimTankComponentProvider extends DimBlockBaseComponentProvider implements IComponentProvider {
//
//    @Override
//    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
//    {
//        super.appendBody(tooltip, accessor, config);
//        if(accessor.getTileEntity() instanceof TileEntityDimTank)
//        {
//            CompoundNBT data = accessor.getServerData();
//
//            String autoEject = new TranslationTextComponent("gui." + Main.MODID + ".eject").getString() + ": ";
//            String yes = new TranslationTextComponent("gui." + Main.MODID + ".yes").getString();
//            String liquid = new TranslationTextComponent("gui." + Main.MODID + ".liquid").getString();
//            String amount = new TranslationTextComponent("gui." + Main.MODID + ".amount").getString();
//
//            if(data.getBoolean(Main.MODID + ".AutoEject"))
//                tooltip.add(new StringTextComponent(autoEject + yes));
//            if(data.getInt(Main.MODID + ".Amount") > 0)
//            {
//                tooltip.add(new StringTextComponent(liquid + " " + data.getString(Main.MODID + ".Liquid")));
//                tooltip.add(new StringTextComponent(amount + " " + data.getInt(Main.MODID + ".Amount") + " mB"));
//            }
//
//        }
//    }
//}