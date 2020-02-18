package edivad.dimstorage.network.packet.tank;

import java.util.function.Supplier;

import com.google.common.collect.Sets;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.network.TankSynchroniser;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PlayerItemTankCacheSync {

	private Frequency[] new_visible, old_visible;

	public PlayerItemTankCacheSync(PacketBuffer buf)
	{
		int size = buf.readInt();
		for(int i = 0; i < size; i++)
			new_visible[i] = new Frequency(buf);

		size = buf.readInt();
		for(int i = 0; i < size; i++)
			old_visible[i] = new Frequency(buf);
	}

	public PlayerItemTankCacheSync(Sets.SetView<Frequency> new_visible, Sets.SetView<Frequency> old_visible)
	{
		this.new_visible = new Frequency [new_visible.size()];
		this.old_visible = new Frequency [old_visible.size()];

		int i = 0;
		for(Frequency frequency : new_visible)
		{
			this.new_visible[i] = frequency.copy();
			i++;
		}

		i = 0;
		for(Frequency frequency : old_visible)
		{
			this.old_visible[i] = frequency.copy();
			i++;
		}
	}

	public void toBytes(PacketBuffer buf)
	{
		buf.writeInt(new_visible.length);
		for(Frequency freq : new_visible)
		{
			freq.writeToPacket(buf);
		}

		buf.writeInt(old_visible.length);
		for(Frequency freq : old_visible)
		{
			freq.writeToPacket(buf);
		}
	}

	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			PlayerEntity player = ctx.get().getSender();
			TankSynchroniser.handleVisiblityPacket(player, new_visible, old_visible);
		});
		ctx.get().setPacketHandled(true);
	}
}
