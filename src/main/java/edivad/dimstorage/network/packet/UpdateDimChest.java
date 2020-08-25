package edivad.dimstorage.network.packet;

import edivad.dimstorage.Main;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.fml.network.NetworkHooks;

public class UpdateDimChest extends UpdateDimBase {

    protected boolean collect;

    public UpdateDimChest(PacketBuffer buf)
    {
        super(buf);
        collect = buf.readBoolean();
    }

    public UpdateDimChest(TileEntityDimChest tile)
    {
        super(tile);
        collect = tile.collect;
    }

    @Override
    public void toBytes(PacketBuffer buf)
    {
        super.toBytes(buf);
        buf.writeBoolean(collect);
    }

    @Override
    public void customHandle(World world, PlayerEntity player)
    {
        TileEntity tile = world.getTileEntity(pos);

        if(!(tile instanceof TileEntityDimChest))
        {
            Main.logger.error("Wrong type of tile entity (expected TileEntityDimChest)!");
            return;
        }

        TileEntityDimChest chest = (TileEntityDimChest) tile;
        chest.setFrequency(freq);
        chest.locked = locked;
        chest.collect = collect;
        chest.markDirty();

        world.notifyBlockUpdate(pos, chest.getBlockState(), chest.getBlockState(), BlockFlags.DEFAULT);
        NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) chest, buf -> buf.writeBlockPos(pos).writeBoolean(true));
    }
}
