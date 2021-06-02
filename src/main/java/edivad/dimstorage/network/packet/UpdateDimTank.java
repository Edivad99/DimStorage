package edivad.dimstorage.network.packet;

import edivad.dimstorage.Main;
import edivad.dimstorage.tile.TileEntityDimTank;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.fml.network.NetworkHooks;

public class UpdateDimTank extends UpdateDimBase {

    protected boolean autoEject;

    public UpdateDimTank(PacketBuffer buf)
    {
        super(buf);
        autoEject = buf.readBoolean();
    }

    public UpdateDimTank(TileEntityDimTank tile)
    {
        super(tile);
        autoEject = tile.autoEject;
    }

    @Override
    public void toBytes(PacketBuffer buf)
    {
        super.toBytes(buf);
        buf.writeBoolean(autoEject);
    }

    @Override
    public void customHandle(World world, ServerPlayerEntity player)
    {
        TileEntity tile = world.getBlockEntity(pos);

        if(!(tile instanceof TileEntityDimTank))
        {
            Main.logger.error("Wrong type of tile entity (expected TileEntityDimTank)!");
            return;
        }

        TileEntityDimTank tank = (TileEntityDimTank) tile;
        tank.setFrequency(freq);
        tank.locked = locked;
        tank.autoEject = autoEject;
        tank.setChanged();

        world.sendBlockUpdated(pos, tank.getBlockState(), tank.getBlockState(), BlockFlags.DEFAULT);
        NetworkHooks.openGui(player, tank, buf -> buf.writeBlockPos(pos).writeBoolean(true));
    }
}
