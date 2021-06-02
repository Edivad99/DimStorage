package edivad.dimstorage.tile;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.Frequency;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class TileFrequencyOwner extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    public boolean locked;

    public TileFrequencyOwner(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
        locked = false;
    }

    private Frequency frequency = new Frequency();
    private int changeCount;

    public void setFrequency(Frequency frequency)
    {
        this.frequency.set(frequency);
        this.setChanged();
        BlockState state = level.getBlockState(worldPosition);
        level.sendBlockUpdated(worldPosition, state, state, BlockFlags.DEFAULT);
    }

    public Frequency getFrequency()
    {
        return frequency.copy();
    }

    @OnlyIn(Dist.CLIENT)
    public void swapOwner()
    {
        if(frequency.hasOwner())
            setFrequency(getFrequency().setPublic());
        else
            setFrequency(getFrequency().setOwner(Main.proxy.getClientPlayer()));
    }

    public void swapLocked()
    {
        locked = !locked;
        this.setChanged();
    }

    public boolean canAccess(PlayerEntity player)
    {
        return frequency.canAccess(player);
    }

    @Override
    public void tick()
    {
        if(getStorage().getChangeCount() > changeCount)
        {
            level.updateNeighbourForOutputSignal(worldPosition, this.getBlockState().getBlock());
            changeCount = getStorage().getChangeCount();
        }
    }

    public abstract AbstractDimStorage getStorage();

    @Override
    public void load(BlockState state, CompoundNBT tag)
    {
        super.load(state, tag);
        frequency.set(new Frequency(tag.getCompound("Frequency")));
        locked = tag.getBoolean("locked");
    }

    @Override
    public CompoundNBT save(CompoundNBT tag)
    {
        super.save(tag);
        tag.put("Frequency", frequency.serializeNBT());
        tag.putBoolean("locked", locked);
        return tag;
    }

    public ActionResultType activate(PlayerEntity player, World worldIn, BlockPos pos, Hand hand)
    {
        if(canAccess(player))
        {
            NetworkHooks.openGui((ServerPlayerEntity) player, this, buf -> buf.writeBlockPos(getBlockPos()).writeBoolean(false));
        }
        else
        {
            player.displayClientMessage(new StringTextComponent(TextFormatting.RED + "Access Denied!"), false);
        }
        return ActionResultType.SUCCESS;
    }

    //Synchronizing on chunk load
    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT tag = super.getUpdateTag();
        tag.put("Frequency", frequency.serializeNBT());
        tag.putBoolean("locked", locked);
        return tag;
    }
    
    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag)
    {
        setFrequency(new Frequency(tag.getCompound("Frequency")));
        locked = tag.getBoolean("locked");
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent(this.getBlockState().getBlock().getDescriptionId());
    }
}
