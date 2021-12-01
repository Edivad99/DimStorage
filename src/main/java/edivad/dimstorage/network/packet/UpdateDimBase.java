package edivad.dimstorage.network.packet;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class UpdateDimBase {

    protected BlockPos pos;
    protected Frequency freq;
    protected boolean locked;

    public UpdateDimBase(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        freq = Frequency.readFromPacket(buf);
        locked = buf.readBoolean();
    }

    public UpdateDimBase(BlockEntityFrequencyOwner tile) {
        pos = tile.getBlockPos();
        freq = tile.getFrequency();
        locked = tile.locked;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        freq.writeToPacket(buf);
        buf.writeBoolean(locked);
    }

    public abstract void customHandle(Level level, ServerPlayer player);

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            Level level = player.level;
            if(level.isLoaded(pos))
                customHandle(level, player);
        });
        ctx.get().setPacketHandled(true);
    }
}
