package edivad.dimstorage.api;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import edivad.dimstorage.Main;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class Frequency {

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
		read_internal(tagCompound);
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

	public UUID getOwnerUUID()
	{
		return owner;
	}

	public String getOwner()
	{
		return ownerText;
	}

	public boolean hasOwner()
	{
		return !ownerText.equals("public") && owner != null;
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
		ownerText = tagCompound.getString("ownerText");
		if(!ownerText.equals("public"))
			owner = tagCompound.getUniqueId("owner");
		else
			owner = null;
		channel = tagCompound.getInt("channel");
		//Main.logger.log(Level.DEBUG, "read_internal: " + this);
		return this;
	}

	public CompoundNBT writeToNBT(CompoundNBT tagCompound)
	{
		write_internal(tagCompound);
		return tagCompound;
	}

	protected CompoundNBT write_internal(CompoundNBT tagCompound)
	{
		tagCompound.putString("ownerText", ownerText);
		if(hasOwner())
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
		return "owner=" + (hasOwner() ? owner : "public") + ",channel=" + channel;
	}

	public Frequency copy()
	{
		return new Frequency(ownerText, owner, channel);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Frequency))
			return false;

		Frequency f = (Frequency) obj;
		return (f.channel == this.channel && f.owner.equals(owner) && f.ownerText.equals(ownerText));
	}

	public Frequency set(Frequency frequency)
	{
		this.ownerText = frequency.ownerText;
		this.owner = frequency.owner;
		this.channel = frequency.channel;
		return this;
	}

	public static Frequency readFromPacket(PacketBuffer buf)
	{
		return new Frequency(buf.readString(32767), buf.readBoolean() ? buf.readUniqueId() : null, buf.readInt());
	}

	public void writeToPacket(PacketBuffer buf)
	{
		buf.writeString(ownerText);
		buf.writeBoolean(hasOwner());
		if(hasOwner())
			buf.writeUniqueId(owner);
		buf.writeInt(channel);
	}

	public boolean canAccess(@Nonnull PlayerEntity player)
	{
		if(!hasOwner())
			return true;
		return getOwnerUUID().equals(player.getUniqueID());
	}
}
