package edivad.dimstorage.api;

import org.apache.logging.log4j.Level;

import edivad.dimstorage.Main;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class Frequency {

	private String owner;
	private int channel;

	public Frequency()
	{
		this(1);
	}

	public Frequency(int channel)
	{
		this("public", channel);
	}

	public Frequency(String owner, int channel)
	{
		this.owner = owner;
		this.channel = channel;
	}

	public Frequency(CompoundNBT tagCompound)
	{
		read_internal(tagCompound);
	}

	public Frequency setOwner(String owner)
	{
		this.owner = owner;
		return this;
	}

	public String getOwner()
	{
		return owner;
	}

	public boolean hasOwner()
	{
		return !owner.equals("public");
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
		owner = tagCompound.getString("owner");
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
		tagCompound.putString("owner", owner);
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
		return "owner=" + owner + ",channel=" + channel;
	}

	public Frequency copy()
	{
		return new Frequency(owner, channel);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Frequency))
			return false;

		Frequency f = (Frequency) obj;
		return (f.channel == this.channel && f.owner == this.owner);
	}

	public Frequency set(Frequency frequency)
	{
		this.owner = frequency.owner;
		this.channel = frequency.channel;
		return this;
	}

	public static Frequency readFromPacket(PacketBuffer buf)
	{
		return new Frequency(buf.readString(32767), buf.readInt());
	}

	public void writeToPacket(PacketBuffer buf)
	{
		buf.writeString(owner);
		buf.writeInt(channel);
	}

	public boolean canAccess(PlayerEntity player)
	{
		if(!hasOwner())
			return true;
		return getOwner().equals(player.getDisplayName().getFormattedText());
	}
}
