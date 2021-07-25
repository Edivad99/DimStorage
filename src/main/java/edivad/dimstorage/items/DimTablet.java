package edivad.dimstorage.items;

import java.util.List;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.container.ContainerDimTablet;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.setup.ModSetup;
import edivad.dimstorage.storage.DimChestStorage;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tools.Config;
import edivad.dimstorage.tools.CustomTranslate;
import edivad.dimstorage.tools.utils.InventoryUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.items.wrapper.InvWrapper;

public class DimTablet extends Item implements MenuProvider {

    public DimTablet()
    {
        super(new Properties().tab(ModSetup.dimStorageTab).stacksTo(1));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
    {
        Level world = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();

        if(!world.isClientSide)
        {
            ItemStack device = player.getItemInHand(context.getHand());
            BlockEntity tile = world.getBlockEntity(pos);
            if(player.isCrouching())
            {
                if(tile instanceof TileEntityDimChest)
                {
                    TileEntityDimChest dimChest = (TileEntityDimChest) tile;
                    if(dimChest.canAccess(player))
                    {
                        CompoundTag tag = new CompoundTag();
                        tag.put("frequency", dimChest.getFrequency().serializeNBT());
                        tag.putBoolean("bound", true);
                        tag.putBoolean("autocollect", false);
                        device.setTag(tag);

                        player.displayClientMessage(new TextComponent(ChatFormatting.GREEN + "Linked to chest"), false);
                        return InteractionResult.SUCCESS;
                    }
                    player.displayClientMessage(new TextComponent(ChatFormatting.RED + "Access Denied!"), false);
                }
                else
                {
                    stack.getTag().putBoolean("autocollect", !stack.getTag().getBoolean("autocollect"));
                    if(stack.getTag().getBoolean("autocollect"))
                        player.displayClientMessage(new TextComponent(ChatFormatting.GREEN + "Enabled autocollect"), false);
                    else
                        player.displayClientMessage(new TextComponent(ChatFormatting.RED + "Disabled autocollect"), false);
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if(!world.isClientSide && hand.compareTo(InteractionHand.MAIN_HAND) == 0)
        {
            if(player.isCrouching())
                return super.use(world, player, hand);
            if(!stack.getOrCreateTag().getBoolean("bound"))
            {
                player.displayClientMessage(new TextComponent(ChatFormatting.RED + "Dimensional Tablet not connected to any DimChest"), false);
                return new InteractionResultHolder<>(InteractionResult.PASS, stack);
            }
            Frequency f = new Frequency(stack.getOrCreateTag().getCompound("frequency"));
            if(!f.canAccess(player))
                return new InteractionResultHolder<>(InteractionResult.PASS, stack);

            NetworkHooks.openGui((ServerPlayer) player, this);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(!worldIn.isClientSide)
        {
            if(stack.getOrCreateTag().getBoolean("autocollect") && stack.getOrCreateTag().getBoolean("bound"))
            {
                if(entityIn instanceof Player)
                {
                    Player pe = (Player) entityIn;
                    Frequency f = new Frequency(stack.getOrCreateTag().getCompound("frequency"));
                    InvWrapper chestInventory = new InvWrapper(getStorage(worldIn, f));

                    for(int i = 0; i < pe.getInventory().getContainerSize(); i++)
                    {
                        Item item = pe.getInventory().getItem(i).getItem();
                        if(Config.DIMTABLET_LIST.get().contains(item.getRegistryName().toString()))
                        {
                            InventoryUtils.mergeItemStack(pe.getInventory().getItem(i), 0, getStorage(worldIn, f).getContainerSize(), chestInventory);
                        }
                    }
                }
            }
        }
    }

    private DimChestStorage getStorage(Level world, Frequency frequency)
    {
        return (DimChestStorage) DimStorageManager.instance(world.isClientSide).getStorage(frequency, "item");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if(worldIn != null)
        {
            if(!stack.hasTag() || !stack.getTag().getBoolean("bound"))
            {
                tooltip.add(CustomTranslate.translateToLocal("message." + Main.MODID + ".adviceToLink"));
                return;
            }

            CompoundTag tag = stack.getTag();
            if(Screen.hasShiftDown())
            {
                Frequency f = new Frequency(tag.getCompound("frequency"));
                tooltip.add(new TranslatableComponent("gui." + Main.MODID + ".frequency").append(" " + f.getChannel()).withStyle(ChatFormatting.GRAY));
                if(f.hasOwner())
                    tooltip.add(new TranslatableComponent("gui." + Main.MODID + ".owner").append(" " + f.getOwner()).withStyle(ChatFormatting.GRAY));

                String yes = new TranslatableComponent("gui." + Main.MODID + ".yes").getString();
                String no = new TranslatableComponent("gui." + Main.MODID + ".no").getString();
                tooltip.add(new TranslatableComponent("gui." + Main.MODID + ".collecting").append(": " + (tag.getBoolean("autocollect") ? yes : no)).withStyle(ChatFormatting.GRAY));
            }
            else
                tooltip.add(CustomTranslate.translateToLocal("message." + Main.MODID + ".holdShift"));

            tooltip.add(CustomTranslate.translateToLocal("message." + Main.MODID + ".changeAutoCollect"));
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity)
    {
        return new ContainerDimTablet(id, playerInventory, playerEntity.level);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent(this.getDescriptionId());
    }
}