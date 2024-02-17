package edivad.dimstorage.api;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class Frequency implements INBTSerializable<CompoundTag> {

  private UUID owner;
  private String ownerText;
  private int channel;

  public Frequency() {
    this(1);
  }

  public Frequency(int channel) {
    this(null, channel);
  }

  public Frequency(@Nullable Player player, int channel) {
    if (player == null) {
      owner = null;
      ownerText = "public";
    } else {
      owner = player.getUUID();
      ownerText = player.getName().getString();
    }
    this.channel = channel;
  }

  private Frequency(String ownerText, @Nullable UUID owner, int channel) {
    this.ownerText = ownerText;
    this.owner = owner;
    this.channel = channel;
  }

  public Frequency(CompoundTag tagCompound) {
    deserializeNBT(tagCompound);
  }

  public static Frequency readFromPacket(FriendlyByteBuf buf) {
    return new Frequency(buf.readUtf(), buf.readBoolean() ? buf.readUUID() : null,
        buf.readVarInt());
  }

  public Frequency set(Frequency frequency) {
    this.ownerText = frequency.ownerText;
    this.owner = frequency.owner;
    this.channel = frequency.channel;
    return this;
  }

  public Frequency copy() {
    return new Frequency(ownerText, owner, channel);
  }

  public Frequency setPublic() {
    owner = null;
    ownerText = "public";
    return this;
  }

  public UUID getOwnerUUID() {
    return owner;
  }

  public String getOwner() {
    return ownerText;
  }

  public Frequency setOwner(@NotNull Player player) {
    owner = player.getUUID();
    ownerText = player.getName().getString();
    return this;
  }

  public int getChannel() {
    return channel;
  }

  public Frequency setChannel(int channel) {
    this.channel = channel;
    return this;
  }

  public boolean hasOwner() {
    return !ownerText.equals("public") && owner != null;
  }

  @Override
  public String toString() {
    return "owner=" + (hasOwner() ? owner : "public") + ",channel=" + channel;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Frequency f)) {
      return false;
    }

    if (f.hasOwner()) {
      return (f.channel == this.channel && f.owner.equals(owner) && f.ownerText.equals(ownerText));
    } else {
      return (f.channel == this.channel && f.ownerText.equals(ownerText));
    }
  }

  public void writeToPacket(FriendlyByteBuf buf) {
    buf.writeUtf(ownerText);
    buf.writeBoolean(hasOwner());
    if (hasOwner()) {
      buf.writeUUID(owner);
    }
    buf.writeVarInt(channel);
  }

  public boolean canAccess(@NotNull Player player) {
    if (!hasOwner()) {
      return true;
    }
    return getOwnerUUID().equals(player.getUUID());
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag tagCompound = new CompoundTag();
    tagCompound.putString("ownerText", ownerText);
    if (hasOwner()) {
      tagCompound.putUUID("owner", owner);
    }
    tagCompound.putInt("channel", channel);
    return tagCompound;
  }

  @Override
  public void deserializeNBT(CompoundTag tagCompound) {
    ownerText = tagCompound.getString("ownerText");
    if (!ownerText.equals("public")) {
      owner = tagCompound.getUUID("owner");
    } else {
      owner = null;
    }
    channel = tagCompound.getInt("channel");
  }
}
