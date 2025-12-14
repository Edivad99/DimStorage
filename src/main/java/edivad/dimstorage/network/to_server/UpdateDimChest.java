package edivad.dimstorage.network.to_server;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blockentities.BlockEntityDimChest;
import edivad.dimstorage.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateDimChest(
    BlockPos pos, Frequency freq, boolean locked) implements CustomPacketPayload {

  public static final Type<UpdateDimChest> TYPE =
      new Type<>(DimStorage.rl("update_dim_chest"));

  public static final StreamCodec<RegistryFriendlyByteBuf, UpdateDimChest> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, UpdateDimChest::pos,
          Frequency.STREAM_CODEC, UpdateDimChest::freq,
          ByteBufCodecs.BOOL, UpdateDimChest::locked,
          UpdateDimChest::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public UpdateDimChest(BlockEntityDimChest tank) {
    this(tank.getBlockPos(), tank.getFrequency(), tank.isLocked());
  }

  public static void handle(UpdateDimChest message, IPayloadContext ctx) {
    var player = ctx.player();
    var level = player.level();
    level.getBlockEntity(message.pos, Registration.DIMCHEST_TILE.get()).ifPresent(chest -> {
      chest.setFrequency(message.freq);
      chest.setLocked(message.locked);
      level.sendBlockUpdated(message.pos, chest.getBlockState(), chest.getBlockState(), Block.UPDATE_ALL);
      player.openMenu(chest, buf -> buf.writeBlockPos(message.pos).writeBoolean(true));
    });
  }
}
