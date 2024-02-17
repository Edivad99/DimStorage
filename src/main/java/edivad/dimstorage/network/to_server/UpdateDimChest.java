package edivad.dimstorage.network.to_server;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blockentities.BlockEntityDimChest;
import edivad.dimstorage.setup.Registration;
import edivad.edivadlib.network.EdivadLibPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record UpdateDimChest(
    BlockPos pos, Frequency freq, boolean locked) implements EdivadLibPacket {

  public static final ResourceLocation ID = DimStorage.rl("update_dim_chest");

  public UpdateDimChest(BlockEntityDimChest tank) {
    this(tank.getBlockPos(), tank.getFrequency(), tank.locked);
  }

  public static UpdateDimChest read(FriendlyByteBuf buf) {
    var pos = buf.readBlockPos();
    var freq = Frequency.readFromPacket(buf);
    var locked = buf.readBoolean();
    return new UpdateDimChest(pos, freq, locked);
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(pos);
    freq.writeToPacket(buf);
    buf.writeBoolean(locked);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext ctx) {
    ctx.player().ifPresent(player -> {
      var level = player.level();
      level.getBlockEntity(pos, Registration.DIMCHEST_TILE.get()).ifPresent(chest -> {
        chest.setFrequency(freq);
        chest.locked = locked;
        chest.setChanged();
        level.sendBlockUpdated(pos, chest.getBlockState(), chest.getBlockState(), Block.UPDATE_ALL);
        player.openMenu(chest, buf -> buf.writeBlockPos(pos).writeBoolean(true));
      });
    });
  }
}
