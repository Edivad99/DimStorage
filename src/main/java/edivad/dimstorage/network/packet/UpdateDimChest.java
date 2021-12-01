package edivad.dimstorage.network.packet;

import edivad.dimstorage.Main;
import edivad.dimstorage.blockentities.BlockEntityDimChest;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkHooks;

public class UpdateDimChest extends UpdateDimBase {

    public UpdateDimChest(FriendlyByteBuf buf) {
        super(buf);
    }

    public UpdateDimChest(BlockEntityDimChest tile) {
        super(tile);
    }

    @Override
    public void customHandle(Level level, ServerPlayer player) {
        BlockEntity tile = level.getBlockEntity(pos);

        if(!(tile instanceof BlockEntityDimChest chest)) {
            Main.logger.error("Wrong type of tile entity (expected TileEntityDimChest)!");
            return;
        }

        chest.setFrequency(freq);
        chest.locked = locked;
        chest.setChanged();

        level.sendBlockUpdated(pos, chest.getBlockState(), chest.getBlockState(), Block.UPDATE_ALL);
        NetworkHooks.openGui(player, chest, buf -> buf.writeBlockPos(pos).writeBoolean(true));
    }
}
