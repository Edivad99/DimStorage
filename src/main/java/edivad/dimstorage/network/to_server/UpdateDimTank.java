package edivad.dimstorage.network.to_server;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.edivadlib.network.EdivadLibPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record UpdateDimTank(
    BlockPos pos, Frequency freq,
    boolean locked, boolean autoEject) implements EdivadLibPacket {

  public static final ResourceLocation ID = DimStorage.rl("update_dim_tank");

  public UpdateDimTank(BlockEntityDimTank tank) {
    this(tank.getBlockPos(), tank.getFrequency(), tank.locked, tank.autoEject);
  }

  public static UpdateDimTank read(FriendlyByteBuf buf) {
    var pos = buf.readBlockPos();
    var freq = Frequency.readFromPacket(buf);
    var locked = buf.readBoolean();
    var autoEject = buf.readBoolean();
    return new UpdateDimTank(pos, freq, locked, autoEject);
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(pos);
    freq.writeToPacket(buf);
    buf.writeBoolean(locked);
    buf.writeBoolean(autoEject);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext ctx) {
    ctx.player().ifPresent(player -> {
      var level = player.level();
      if (level.getBlockEntity(pos) instanceof BlockEntityDimTank tank) {
        tank.setFrequency(freq);
        tank.locked = locked;
        tank.autoEject = autoEject;
        tank.setChanged();
        level.sendBlockUpdated(pos, tank.getBlockState(), tank.getBlockState(), Block.UPDATE_ALL);
        player.openMenu(tank, buf -> buf.writeBlockPos(pos).writeBoolean(true));
      }
    });
  }
}
