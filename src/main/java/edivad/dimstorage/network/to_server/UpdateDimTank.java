package edivad.dimstorage.network.to_server;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateDimTank(
    BlockPos pos, Frequency freq,
    boolean locked, boolean autoEject) implements CustomPacketPayload {

  public static final Type<UpdateDimTank> TYPE =
      new Type<>(DimStorage.rl("update_dim_tank"));

  public static final StreamCodec<RegistryFriendlyByteBuf, UpdateDimTank> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, UpdateDimTank::pos,
          Frequency.STREAM_CODEC, UpdateDimTank::freq,
          ByteBufCodecs.BOOL, UpdateDimTank::locked,
          ByteBufCodecs.BOOL, UpdateDimTank::autoEject,
          UpdateDimTank::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public UpdateDimTank(BlockEntityDimTank tank) {
    this(tank.getBlockPos(), tank.getFrequency(), tank.isLocked(), tank.autoEject);
  }

  public static void handle(UpdateDimTank message, IPayloadContext ctx) {
    var player = ctx.player();
    var level = player.level();
    level.getBlockEntity(message.pos, Registration.DIMTANK_TILE.get()).ifPresent(tank -> {
      tank.setFrequency(message.freq);
      tank.setLocked(message.locked);
      tank.autoEject = message.autoEject;
      level.sendBlockUpdated(message.pos, tank.getBlockState(), tank.getBlockState(), Block.UPDATE_ALL);
      player.openMenu(tank, buf -> buf.writeBlockPos(message.pos).writeBoolean(true));
    });
  }
}
