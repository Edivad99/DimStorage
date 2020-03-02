package edivad.dimstorage.network.packet;

import java.util.function.Supplier;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.storage.DimChestStorage;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class OpenChest {

	private Frequency freq;
	private boolean open;

	public OpenChest(PacketBuffer buf)
	{
		freq = Frequency.readFromPacket(buf);
		open = buf.readBoolean();
	}

	public OpenChest(Frequency freq, boolean open)
	{
		this.freq = freq;
		this.open = open;
	}

	public void toBytes(PacketBuffer buf)
	{
		freq.writeToPacket(buf);
		buf.writeBoolean(open);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() -> {
			((DimChestStorage) DimStorageManager.instance(true).getStorage(freq, "item")).setClientOpen(open ? 1 : 0);
		});
		ctx.get().setPacketHandled(true);
	}
}
