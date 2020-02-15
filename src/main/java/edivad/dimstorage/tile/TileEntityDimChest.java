package edivad.dimstorage.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.container.ContainerDimChest;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.storage.DimChestStorage;
import edivad.dimstorage.tools.Config;
import edivad.dimstorage.tools.Message;
import edivad.dimstorage.tools.Message.Messages;
import edivad.dimstorage.tools.extra.InventoryUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class TileEntityDimChest extends TileFrequencyOwner {

	private static final float MIN_MOVABLE_POSITION = 0f;
	private static final float MAX_MOVABLE_POSITION = 0.5f;
	private static final float OPENING_SPEED = 0.05f;

	private int openCount;
	public float movablePartState;
	public int rotation;

	public boolean locked;

	public static final int AREA = Config.DIMCHEST_AREA.get();
	public boolean collect;
	private List<BlockPos> blockNeighbors;

	public TileEntityDimChest()
	{
		super(Registration.DIMCHEST_TILE.get());
		movablePartState = MIN_MOVABLE_POSITION;
		locked = false;
		collect = false;

		blockNeighbors = new ArrayList<>();
	}

	@Override
	public void tick()
	{
		super.tick();

		if(!world.isRemote && (world.getGameTime() % 20 == 0 || openCount != getStorage().getNumOpen()))
		{
			openCount = getStorage().getNumOpen();
			world.addBlockEvent(getPos(), this.getBlockState().getBlock(), 1, openCount);
			world.notifyNeighborsOfStateChange(pos, this.getBlockState().getBlock());
			if(collect)
				checkNeighbors();
		}

		if(this.openCount > 0)
		{
			if(this.movablePartState < MAX_MOVABLE_POSITION)
				this.movablePartState += OPENING_SPEED;
			else
				this.movablePartState = MAX_MOVABLE_POSITION;
		}
		else
		{
			if(this.movablePartState > MIN_MOVABLE_POSITION)
				this.movablePartState -= OPENING_SPEED;
			else
				this.movablePartState = MIN_MOVABLE_POSITION;
		}
	}

	private List<BlockPos> getChunkNeighbors(int area)
	{
		int range = area / 2;
		return BlockPos.getAllInBox(getPos().add(-range, 0, -range), getPos().add(range, 0, range)).map(BlockPos::toImmutable).collect(Collectors.toList());
	}

	private void checkNeighbors()
	{
		if(blockNeighbors.isEmpty())
			blockNeighbors = getChunkNeighbors(AREA);

		for(BlockPos pos : blockNeighbors)
		{
			BlockState block = world.getBlockState(pos);
			if(block.hasTileEntity())
			{
				TileEntity te = world.getTileEntity(pos);
				if(!(te instanceof TileFrequencyOwner))
				{
					processInventory(te);
				}
			}
		}
	}

	private void processInventory(TileEntity te)
	{
		IItemHandler handler = getItemHandler(te);
		if(handler != null)
		{
			InvWrapper myInventory = new InvWrapper(getStorage());
			int size = handler.getSlots();
			//TODO: To avoid that DimChest find fuel inside the furnace
			if(te instanceof AbstractFurnaceTileEntity)
				size--;
			for(int i = 0; i < size; i++)
			{
				if(!handler.getStackInSlot(i).isEmpty())
					InventoryUtils.mergeItemStack(handler.getStackInSlot(i), 0, getStorage().getSizeInventory(), myInventory);
			}
		}
	}

	private IItemHandler getItemHandler(@Nonnull TileEntity tile)
	{
		IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
		//System.out.println(handler);
		if(handler == null)
		{
			if(tile instanceof ISidedInventory)
			{
				handler = new SidedInvWrapper((ISidedInventory) tile, Direction.DOWN);
			}
			else if(tile instanceof IInventory)
			{
				handler = new InvWrapper((IInventory) tile);
			}
		}
		return handler;
	}

	public void swapLocked()
	{
		locked = !locked;
		this.markDirty();
	}

	public void swapCollect()
	{
		collect = !collect;
		this.markDirty();
	}

	@Override
	public boolean receiveClientEvent(int id, int type)
	{
		if(id == 1)
		{
			openCount = type;
			return true;
		}
		return false;
	}

	@Override
	public DimChestStorage getStorage()
	{
		return (DimChestStorage) DimStorageManager.instance(world.isRemote).getStorage(frequency, "item");
	}

	@Override
	public void onPlaced(LivingEntity entity)
	{
		rotation = (int) Math.floor(entity.rotationYaw * 4 / 360 + 2.5D) & 3;
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		super.write(tag);
		tag.putByte("rot", (byte) rotation);
		tag.putBoolean("locked", locked);
		tag.putBoolean("collect", collect);
		return tag;
	}

	@Override
	public void read(CompoundNBT tag)
	{
		super.read(tag);
		rotation = tag.getByte("rot") & 3;
		locked = tag.getBoolean("locked");
		collect = tag.getBoolean("collect");
	}

	@Override
	public boolean activate(PlayerEntity player, World worldIn, BlockPos pos)
	{
		if(canAccess(player))
		{
			NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) this, buf -> buf.writeBlockPos(getPos()).writeBoolean(false));
		}
		else
		{
			Message.sendChatMessage(player, Messages.ACCESSDENIED);
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing)
	{
		if(!locked && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return LazyOptional.of(() -> (T) new InvWrapper(getStorage()));
		}
		return super.getCapability(capability, facing);
	}

	//Synchronizing on block update
	@Override
	public final SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT root = new CompoundNBT();
		root.put("Frequency", frequency.writeToNBT(new CompoundNBT()));
		root.putByte("rot", (byte) rotation);
		root.putBoolean("locked", locked);
		root.putBoolean("collect", collect);
		return new SUpdateTileEntityPacket(getPos(), 1, root);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		CompoundNBT tag = pkt.getNbtCompound();
		frequency.set(new Frequency(tag.getCompound("Frequency")));
		rotation = tag.getByte("rot") & 3;
		locked = tag.getBoolean("locked");
		collect = tag.getBoolean("collect");
	}

	//Synchronizing on chunk load
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT tag = super.getUpdateTag();
		tag.putByte("rot", (byte) rotation);
		tag.putBoolean("locked", locked);
		tag.putBoolean("collect", collect);
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag)
	{
		super.handleUpdateTag(tag);
		rotation = tag.getByte("rot") & 3;
		locked = tag.getBoolean("locked");
		collect = tag.getBoolean("collect");
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey());
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity)
	{
		return new ContainerDimChest(id, playerInventory, this, false);
	}
}
