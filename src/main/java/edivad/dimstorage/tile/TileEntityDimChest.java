package edivad.dimstorage.tile;

import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.container.ContainerDimChest;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.storage.DimChestStorage;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TileEntityDimChest extends TileFrequencyOwner {

    private static final float MIN_MOVABLE_POSITION = 0f;
    private static final float MAX_MOVABLE_POSITION = 0.5f;
    private static final float OPENING_SPEED = 0.05f;

    private int openCount;
    public float movablePartState;
    public int rotation;

    //Set the Capability
    private LazyOptional<IItemHandler> itemHandler = LazyOptional.empty();

    public TileEntityDimChest(BlockPos blockPos, BlockState blockState)
    {
        super(Registration.DIMCHEST_TILE.get(), blockPos, blockState);
        movablePartState = MIN_MOVABLE_POSITION;
    }

    @Override
    public void onServerTick(Level level, BlockPos blockPos, BlockState blockState)
    {
        if(level.getGameTime() % 20 == 0 || openCount != getStorage().getNumOpen())
        {
            openCount = getStorage().getNumOpen();
            level.blockEvent(getBlockPos(), this.getBlockState().getBlock(), 1, openCount);
            level.updateNeighborsAt(worldPosition, this.getBlockState().getBlock());
        }

        if(openCount > 0)
        {
            if(movablePartState < MAX_MOVABLE_POSITION)
                movablePartState += OPENING_SPEED;
            else
                movablePartState = MAX_MOVABLE_POSITION;
        }
        else
        {
            if(movablePartState > MIN_MOVABLE_POSITION)
                movablePartState -= OPENING_SPEED;
            else
                movablePartState = MIN_MOVABLE_POSITION;
        }
    }

    public int getComparatorInput()
    {
        return itemHandler.map(ItemHandlerHelper::calcRedstoneFromInventory).orElse(0);
    }

    @Override
    public void setFrequency(Frequency frequency)
    {
        super.setFrequency(frequency);
        itemHandler.invalidate();
        itemHandler = LazyOptional.of(() -> new InvWrapper(getStorage()));
    }

    @Override
    public void setLevel(Level level)
    {
        super.setLevel(level);
        itemHandler.invalidate();
        itemHandler = LazyOptional.of(() -> new InvWrapper(getStorage()));
    }

    @Override
    public void setRemoved()
    {
        super.setRemoved();
        itemHandler.invalidate();
    }

    @Override
    public boolean triggerEvent(int id, int type)
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
        return (DimChestStorage) DimStorageManager.instance(level.isClientSide).getStorage(getFrequency(), "item");
    }

    public void onPlaced(LivingEntity entity)
    {
        rotation = (int) Math.floor(entity.getYRot() * 4 / 360 + 2.5D) & 3;
    }

    @Override
    public CompoundTag save(CompoundTag tag)
    {
        super.save(tag);
        tag.putByte("rot", (byte) rotation);
        return tag;
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        rotation = tag.getByte("rot") & 3;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing)
    {
        if(!locked && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemHandler.cast();
        return super.getCapability(capability, facing);
    }

    //Synchronizing on block update
    @Override
    public final ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        CompoundTag root = new CompoundTag();
        root.put("Frequency", getFrequency().serializeNBT());
        root.putBoolean("locked", locked);
        root.putByte("rot", (byte) rotation);
        return new ClientboundBlockEntityDataPacket(getBlockPos(), 1, root);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        CompoundTag tag = pkt.getTag();
        setFrequency(new Frequency(tag.getCompound("Frequency")));
        locked = tag.getBoolean("locked");
        rotation = tag.getByte("rot") & 3;
    }

    //Synchronizing on chunk load
    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag tag = super.getUpdateTag();
        tag.putByte("rot", (byte) rotation);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag)
    {
        super.handleUpdateTag(tag);
        rotation = tag.getByte("rot") & 3;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity)
    {
        return new ContainerDimChest(id, playerInventory, this, false);
    }
}
