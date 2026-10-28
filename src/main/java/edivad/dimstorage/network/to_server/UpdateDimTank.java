package edivad.dimstorage.network.to_server;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentity.BlockEntityDimTank;
import edivad.dimstorage.menu.DimTankMenu;
import edivad.dimstorage.setup.ModRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateDimTank(
    BlockPos pos, int channel, boolean privateFrequency,
    boolean locked, boolean autoEject) implements CustomPacketPayload {

  public static final Type<UpdateDimTank> TYPE =
      new Type<>(DimStorage.id("update_dim_tank"));

  public static final StreamCodec<RegistryFriendlyByteBuf, UpdateDimTank> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, UpdateDimTank::pos,
          ByteBufCodecs.VAR_INT, UpdateDimTank::channel,
          ByteBufCodecs.BOOL, UpdateDimTank::privateFrequency,
          ByteBufCodecs.BOOL, UpdateDimTank::locked,
          ByteBufCodecs.BOOL, UpdateDimTank::autoEject,
          UpdateDimTank::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public UpdateDimTank(BlockEntityDimTank tank) {
    this(tank.getBlockPos(), tank.getFrequency().channel(), tank.getFrequency().hasOwner(),
        tank.isLocked(), tank.autoEject);
  }

  public static void handle(UpdateDimTank message, IPayloadContext ctx) {
    if (!(ctx.player() instanceof ServerPlayer player)) {
      return;
    }
    var level = player.level();
    level.getBlockEntity(message.pos, ModRegistration.DIMTANK_TILE.get()).ifPresent(tank -> {
      // Only the player who has this tank's menu open may change its settings
      if (!(player.containerMenu instanceof DimTankMenu menu) || menu.owner != tank
          || !tank.stillValid(player)) {
        return;
      }
      tank.applyClientSettings(player, message.channel, message.privateFrequency,
          message.locked);
      tank.setAutoEject(message.autoEject);
      level.sendBlockUpdated(message.pos, tank.getBlockState(), tank.getBlockState(), Block.UPDATE_ALL);
      player.openMenu(tank, buf -> buf.writeBlockPos(message.pos).writeBoolean(true));
    });
  }
}
