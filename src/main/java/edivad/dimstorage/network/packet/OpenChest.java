package edivad.dimstorage.network.packet;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.network.handler.MessageHandlerServerToPlayer;
import edivad.dimstorage.storage.DimChestStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class OpenChest extends MessageHandlerServerToPlayer<OpenChest> implements IMessage {

	private Frequency freq;
	private boolean open;

	public OpenChest()
	{
	}

	public OpenChest(Frequency freq, boolean open)
	{
		this.freq = freq;
		this.open = open;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		freq = new Frequency(ByteBufUtils.readUTF8String(buf), buf.readInt());
		open = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, freq.getOwner());
		buf.writeInt(freq.getChannel());
		
		buf.writeBoolean(open);
	}

	@Override
	protected void handle(OpenChest msg, World world, EntityPlayer player)
	{
		((DimChestStorage) DimStorageManager.instance(true).getStorage(msg.freq, "item")).setClientOpen(msg.open ? 1 : 0);
	}

}
