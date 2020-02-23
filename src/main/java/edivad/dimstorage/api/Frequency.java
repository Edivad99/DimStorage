package edivad.dimstorage.api;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import edivad.dimstorage.Main;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class Frequency {

	private UUID owner;
	private boolean isPublic;
	private int channel;

	public Frequency()
	{
		this(1);
	}

	public Frequency(int channel)
	{
		this(true, null, channel);
	}

	public Frequency(boolean isPublic, @Nullable UUID owner, int channel)
	{
		if((isPublic && owner != null) || (!isPublic && owner == null))
			throw new RuntimeException();
		this.isPublic = isPublic;
		this.owner = owner;
		this.channel = channel;
	}

	public Frequency(CompoundNBT tagCompound)
	{
		read_internal(tagCompound);
	}

	public Frequency setOwner(@Nonnull PlayerEntity player)
	{
		isPublic = false;
		owner = player.getUniqueID();
		return this;
	}

	public Frequency setPublic()
	{
		isPublic = true;
		owner = null;
		return this;
	}

	public UUID getOwnerUUID()
	{
		return owner;
	}

	public String getOwner()
	{
		if(owner != null)
		{
			ServerPlayerEntity spe = Main.getServer().getPlayerList().getPlayerByUUID(owner);
			if(spe != null)
				return spe.getName().getFormattedText();
			else
				return "undefined";
		}
		return "public";
	}

	public boolean hasOwner()
	{
		return !isPublic;
	}

	public Frequency setChannel(int channel)
	{
		this.channel = channel;
		return this;
	}

	public int getChannel()
	{
		return channel;
	}

	protected Frequency read_internal(CompoundNBT tagCompound)
	{
		isPublic = tagCompound.getBoolean("isPublic");
		if(!isPublic)
			owner = tagCompound.getUniqueId("owner");
		else
			owner = null;
		channel = tagCompound.getInt("channel");
		Main.logger.log(Level.DEBUG, "read_internal: " + this);
		return this;
	}

	public CompoundNBT writeToNBT(CompoundNBT tagCompound)
	{
		write_internal(tagCompound);
		return tagCompound;
	}

	protected CompoundNBT write_internal(CompoundNBT tagCompound)
	{
		tagCompound.putBoolean("isPublic", isPublic);
		if(!isPublic)
			tagCompound.putUniqueId("owner", owner);
		tagCompound.putInt("channel", channel);
		return tagCompound;
	}

	public static Frequency readFromStack(ItemStack stack)
	{
		if(stack.hasTag())
		{
			CompoundNBT stackTag = stack.getTag();
			if(stackTag.contains("Frequency"))
			{
				return new Frequency(stackTag.getCompound("Frequency"));
			}
		}
		return new Frequency();
	}

	public ItemStack writeToStack(ItemStack stack)
	{
		CompoundNBT tagCompound;
		if(!stack.hasTag())
			stack.setTag(new CompoundNBT());

		tagCompound = stack.getTag();
		tagCompound.put("Frequency", write_internal(new CompoundNBT()));
		return stack;
	}

	@Override
	public String toString()
	{
		return "owner=" + (isPublic ? "public" : owner) + ",channel=" + channel;
	}

	public Frequency copy()
	{
		return new Frequency(isPublic, owner, channel);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Frequency))
			return false;

		Frequency f = (Frequency) obj;
		return (f.channel == this.channel && f.owner == this.owner && f.isPublic == this.isPublic);
	}

	public Frequency set(Frequency frequency)
	{
		this.isPublic = frequency.isPublic;
		this.owner = frequency.owner;
		this.channel = frequency.channel;
		return this;
	}

	public static Frequency getFromPacket(PacketBuffer buf)
	{
		boolean isPublic = buf.readBoolean();
		return new Frequency(isPublic, isPublic ? null : buf.readUniqueId(), buf.readInt());
	}

	public void writeToPacket(PacketBuffer buf)
	{
		buf.writeBoolean(isPublic);
		if(!isPublic)
			buf.writeUniqueId(owner);
		buf.writeInt(channel);
	}

	public boolean canAccess(PlayerEntity player)
	{
		if(!hasOwner())
			return true;
		return getOwnerUUID().equals(player.getUniqueID());
	}
}
