package edivad.dimstorage.compat.waila;

//import edivad.dimstorage.tile.TileEntityDimChest;
//import mcp.mobius.waila.api.IServerDataProvider;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.world.World;
//
//public class DimChestDataProvider extends DimBlockBaseProvider implements IServerDataProvider<TileEntity> {
//
//    @Override
//    public void appendServerData(CompoundNBT compoundNBT, ServerPlayerEntity serverPlayerEntity, World world, TileEntity tileEntity)
//    {
//        super.appendServerData(compoundNBT, serverPlayerEntity, world, tileEntity);
//        if(tileEntity instanceof TileEntityDimChest)
//        {
//            TileEntityDimChest tile = (TileEntityDimChest) tileEntity;
//            compoundNBT.putBoolean("Collecting", tile.collect);
//        }
//    }
//}