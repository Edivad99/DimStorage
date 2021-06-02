package edivad.dimstorage.network.packet;

import java.util.function.Supplier;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.tile.TileFrequencyOwner;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public abstract class UpdateDimBase {

    protected BlockPos pos;
    protected Frequency freq;
    protected boolean locked;

    public UpdateDimBase(PacketBuffer buf)
    {
        pos = buf.readBlockPos();
        freq = Frequency.readFromPacket(buf);
        locked = buf.readBoolean();
    }

    public UpdateDimBase(TileFrequencyOwner tile)
    {
        pos = tile.getBlockPos();
        freq = tile.getFrequency();
        locked = tile.locked;
    }

    public void toBytes(PacketBuffer buf)
    {
        buf.writeBlockPos(pos);
        freq.writeToPacket(buf);
        buf.writeBoolean(locked);
    }

    public abstract void customHandle(World world, ServerPlayerEntity player);

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            World world = player.level;
            if(world.isLoaded(pos))
                customHandle(world, player);
        });
        ctx.get().setPacketHandled(true);
    }
}
