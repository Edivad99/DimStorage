package edivad.dimstorage.network.to_server;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentity.BlockEntityDimChest;
import edivad.dimstorage.menu.DimChestMenu;
import edivad.dimstorage.setup.ModRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateDimChest(
    BlockPos pos, int channel, boolean privateFrequency,
    boolean locked) implements CustomPacketPayload {

  public static final Type<UpdateDimChest> TYPE =
      new Type<>(DimStorage.id("update_dim_chest"));

  public static final StreamCodec<RegistryFriendlyByteBuf, UpdateDimChest> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, UpdateDimChest::pos,
          ByteBufCodecs.VAR_INT, UpdateDimChest::channel,
          ByteBufCodecs.BOOL, UpdateDimChest::privateFrequency,
          ByteBufCodecs.BOOL, UpdateDimChest::locked,
          UpdateDimChest::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public UpdateDimChest(BlockEntityDimChest chest) {
    this(chest.getBlockPos(), chest.getFrequency().channel(), chest.getFrequency().hasOwner(),
        chest.isLocked());
  }

  public static void handle(UpdateDimChest message, IPayloadContext ctx) {
    if (!(ctx.player() instanceof ServerPlayer player)) {
      return;
    }
    var level = player.level();
    level.getBlockEntity(message.pos, ModRegistration.DIMCHEST_TILE.get()).ifPresent(chest -> {
      // Only the player who has this chest's menu open may change its settings
      if (!(player.containerMenu instanceof DimChestMenu menu) || menu.owner != chest
          || !chest.stillValid(player)) {
        return;
      }
      chest.applyClientSettings(player, message.channel, message.privateFrequency,
          message.locked);
      level.sendBlockUpdated(message.pos, chest.getBlockState(), chest.getBlockState(), Block.UPDATE_ALL);
      player.openMenu(chest, buf -> buf.writeBlockPos(message.pos).writeBoolean(true));
    });
  }
}
