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

    public UpdateDimChest(BlockEntityDimChest blockentity) {
        super(blockentity);
    }

    @Override
    public void customHandle(Level level, ServerPlayer player) {
        BlockEntity blockentity = level.getBlockEntity(pos);

        if(!(blockentity instanceof BlockEntityDimChest chest)) {
            Main.LOGGER.error("Wrong type of blockentity (expected BlockEntityDimChest)!");
            return;
        }

        chest.setFrequency(freq);
        chest.locked = locked;
        chest.setChanged();

        level.sendBlockUpdated(pos, chest.getBlockState(), chest.getBlockState(), Block.UPDATE_ALL);
        NetworkHooks.openScreen(player, chest, buf -> buf.writeBlockPos(pos).writeBoolean(true));
    }
}
