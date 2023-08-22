package edivad.dimstorage.network.packet;

import edivad.dimstorage.DimStorage;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.NetworkHooks;

public class UpdateDimTank extends UpdateDimBase {

  protected boolean autoEject;

  public UpdateDimTank(FriendlyByteBuf buf) {
    super(buf);
    autoEject = buf.readBoolean();
  }

  public UpdateDimTank(BlockEntityDimTank blockentity) {
    super(blockentity);
    autoEject = blockentity.autoEject;
  }

  @Override
  public void encode(FriendlyByteBuf buf) {
    super.encode(buf);
    buf.writeBoolean(autoEject);
  }

  @Override
  public void customHandle(Level level, ServerPlayer player) {
    var blockentity = level.getBlockEntity(pos);

    if (!(blockentity instanceof BlockEntityDimTank tank)) {
      DimStorage.LOGGER.error("Wrong type of blockentity (expected BlockEntityDimTank)!");
      return;
    }

    tank.setFrequency(freq);
    tank.locked = locked;
    tank.autoEject = autoEject;
    tank.setChanged();

    level.sendBlockUpdated(pos, tank.getBlockState(), tank.getBlockState(), Block.UPDATE_ALL);
    NetworkHooks.openScreen(player, tank, buf -> buf.writeBlockPos(pos).writeBoolean(true));
  }
}
