package edivad.dimstorage.network.packet;

import java.util.function.Supplier;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blockentities.BlockEntityFrequencyOwner;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public abstract class UpdateDimBase {

  protected BlockPos pos;
  protected Frequency freq;
  protected boolean locked;

  public UpdateDimBase(FriendlyByteBuf buf) {
    pos = buf.readBlockPos();
    freq = Frequency.readFromPacket(buf);
    locked = buf.readBoolean();
  }

  public UpdateDimBase(BlockEntityFrequencyOwner blockentity) {
    pos = blockentity.getBlockPos();
    freq = blockentity.getFrequency();
    locked = blockentity.locked;
  }

  public void encode(FriendlyByteBuf buf) {
    buf.writeBlockPos(pos);
    freq.writeToPacket(buf);
    buf.writeBoolean(locked);
  }

  public abstract void customHandle(Level level, ServerPlayer player);

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      var player = ctx.get().getSender();
      var level = player.level();
      if (level.isLoaded(pos)) {
        customHandle(level, player);
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
