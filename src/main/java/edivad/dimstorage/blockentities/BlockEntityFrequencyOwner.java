package edivad.dimstorage.blockentities;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.AbstractDimStorage;
import edivad.dimstorage.api.Frequency;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

public abstract class BlockEntityFrequencyOwner extends BlockEntity implements MenuProvider {

    public boolean locked;

    public BlockEntityFrequencyOwner(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
        locked = false;
    }

    private Frequency frequency = new Frequency();
    private int changeCount;

    public void setFrequency(Frequency frequency) {
        this.frequency.set(frequency);
        this.setChanged();
        BlockState state = level.getBlockState(worldPosition);
        level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
    }

    public Frequency getFrequency() {
        return frequency.copy();
    }

    @OnlyIn(Dist.CLIENT)
    public void swapOwner() {
        if(frequency.hasOwner())
            setFrequency(getFrequency().setPublic());
        else
            setFrequency(getFrequency().setOwner(Minecraft.getInstance().player));
    }

    public void swapLocked() {
        locked = !locked;
        this.setChanged();
    }

    public boolean canAccess(Player player) {
        return frequency.canAccess(player);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BlockEntityFrequencyOwner blockentity) {
        if(blockentity.getStorage().getChangeCount() > blockentity.changeCount) {
            level.updateNeighbourForOutputSignal(blockentity.worldPosition, blockentity.getBlockState().getBlock());
            blockentity.changeCount = blockentity.getStorage().getChangeCount();
        }
        blockentity.onServerTick(level, pos, state);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, BlockEntityFrequencyOwner blockentity) {
        blockentity.onClientTick(level, pos, state);
    }

    public abstract AbstractDimStorage getStorage();

    public abstract void onServerTick(Level level, BlockPos pos, BlockState state);

    public abstract void onClientTick(Level level, BlockPos pos, BlockState state);

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        frequency.set(new Frequency(tag.getCompound("Frequency")));
        locked = tag.getBoolean("locked");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Frequency", frequency.serializeNBT());
        tag.putBoolean("locked", locked);
    }

    public InteractionResult activate(Player player, Level level, BlockPos pos, InteractionHand hand) {
        if(canAccess(player)) {
            NetworkHooks.openGui((ServerPlayer) player, this, buf -> buf.writeBlockPos(getBlockPos()).writeBoolean(false));
        }
        else {
            player.displayClientMessage(new TextComponent(ChatFormatting.RED + "Access Denied!"), false);
        }
        return InteractionResult.SUCCESS;
    }

    //Synchronizing on chunk load
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("Frequency", frequency.serializeNBT());
        tag.putBoolean("locked", locked);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        setFrequency(new Frequency(tag.getCompound("Frequency")));
        locked = tag.getBoolean("locked");
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
    }
}
