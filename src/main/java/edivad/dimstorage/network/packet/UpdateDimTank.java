package edivad.dimstorage.network.packet;

import edivad.dimstorage.Main;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class UpdateDimTank extends UpdateDimBase {

    protected boolean autoEject;

    public UpdateDimTank(FriendlyByteBuf buf) {
        super(buf);
        autoEject = buf.readBoolean();
    }

    public UpdateDimTank(BlockEntityDimTank tile) {
        super(tile);
        autoEject = tile.autoEject;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(autoEject);
    }

    @Override
    public void customHandle(Level level, ServerPlayer player) {
        BlockEntity tile = level.getBlockEntity(pos);

        if(!(tile instanceof BlockEntityDimTank tank)) {
            Main.logger.error("Wrong type of tile entity (expected TileEntityDimTank)!");
            return;
        }

        tank.setFrequency(freq);
        tank.locked = locked;
        tank.autoEject = autoEject;
        tank.setChanged();

        level.sendBlockUpdated(pos, tank.getBlockState(), tank.getBlockState(), BlockFlags.DEFAULT);
        NetworkHooks.openGui(player, tank, buf -> buf.writeBlockPos(pos).writeBoolean(true));
    }
}
