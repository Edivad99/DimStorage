//package edivad.dimstorage.compat.waila;
//
//import edivad.dimstorage.Main;
//import edivad.dimstorage.tile.TileEntityDimTank;
//import mcp.mobius.waila.api.IServerDataProvider;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.world.World;
//
//public class DimTankDataProvider extends DimBlockBaseProvider implements IServerDataProvider<TileEntity> {
//
//    @Override
//    public void appendServerData(CompoundNBT compoundNBT, ServerPlayerEntity serverPlayerEntity, World world, TileEntity tileEntity)
//    {
//        super.appendServerData(compoundNBT, serverPlayerEntity, world, tileEntity);
//        if(tileEntity instanceof TileEntityDimTank)
//        {
//            TileEntityDimTank tile = (TileEntityDimTank) tileEntity;
//            compoundNBT.putBoolean(Main.MODID + ".AutoEject", tile.autoEject);
//            String liquidName = tile.liquidState.serverLiquid.getDisplayName().getString();
//            int liquidLevel = tile.liquidState.serverLiquid.getAmount();
//            compoundNBT.putString(Main.MODID + ".Liquid", liquidName);
//            compoundNBT.putInt(Main.MODID + ".Amount", liquidLevel);
//        }
//    }
//}