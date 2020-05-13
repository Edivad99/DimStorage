package edivad.dimstorage.api;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.INBTSerializable;

public class Frequency implements INBTSerializable<CompoundNBT> {

	private UUID owner;
	private String ownerText;
	private int channel;

	public Frequency()
	{
		this(1);
	}

	public Frequency(int channel)
	{
		this(null, channel);
	}

	public Frequency(@Nullable PlayerEntity player, int channel)
	{
		if(player == null)
		{
			owner = null;
			ownerText = "public";
		}
		else
		{
			owner = player.getUniqueID();
			ownerText = player.getName().getFormattedText();
		}
		this.channel = channel;
	}

	private Frequency(String ownerText, @Nullable UUID owner, int channel)
	{
		this.ownerText = ownerText;
		this.owner = owner;
		this.channel = channel;
	}

	public Frequency(CompoundNBT tagCompound)
	{
		deserializeNBT(tagCompound);
	}

	public Frequency set(Frequency frequency)
	{
		this.ownerText = frequency.ownerText;
		this.owner = frequency.owner;
		this.channel = frequency.channel;
		return this;
	}

	public Frequency copy()
	{
		return new Frequency(ownerText, owner, channel);
	}

	public Frequency setOwner(@Nonnull PlayerEntity player)
	{
		owner = player.getUniqueID();
		ownerText = player.getName().getFormattedText();
		return this;
	}

	public Frequency setPublic()
	{
		owner = null;
		ownerText = "public";
		return this;
	}

	public Frequency setChannel(int channel)
	{
		this.channel = channel;
		return this;
	}

	public UUID getOwnerUUID()
	{
		return owner;
	}

	public String getOwner()
	{
		return ownerText;
	}

	public int getChannel()
	{
		return channel;
	}

	public boolean hasOwner()
	{
		return !ownerText.equals("public") && owner != null;
	}

	@Override
	public String toString()
	{
		return "owner=" + (hasOwner() ? owner : "public") + ",channel=" + channel;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Frequency))
			return false;

		Frequency f = (Frequency) obj;
		if(f.hasOwner())
			return (f.channel == this.channel && f.owner.equals(owner) && f.ownerText.equals(ownerText));
		else
			return (f.channel == this.channel && f.ownerText.equals(ownerText));
	}

	public static Frequency readFromPacket(PacketBuffer buf)
	{
		return new Frequency(buf.readString(32767), buf.readBoolean() ? buf.readUniqueId() : null, buf.readVarInt());
	}

	public void writeToPacket(PacketBuffer buf)
	{
		buf.writeString(ownerText);
		buf.writeBoolean(hasOwner());
		if(hasOwner())
			buf.writeUniqueId(owner);
		buf.writeVarInt(channel);
	}

	public boolean canAccess(@Nonnull PlayerEntity player)
	{
		if(!hasOwner())
			return true;
		return getOwnerUUID().equals(player.getUniqueID());
	}

	@Override
	public CompoundNBT serializeNBT()
	{
		CompoundNBT tagCompound = new CompoundNBT();
		tagCompound.putString("ownerText", ownerText);
		if(hasOwner())
			tagCompound.putUniqueId("owner", owner);
		tagCompound.putInt("channel", channel);
		return tagCompound;
	}

	@Override
	public void deserializeNBT(CompoundNBT tagCompound)
	{
		ownerText = tagCompound.getString("ownerText");
		if(!ownerText.equals("public"))
			owner = tagCompound.getUniqueId("owner");
		else
			owner = null;
		channel = tagCompound.getInt("channel");
	}
}
