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
import edivad.dimstorage.tools.Translate;
import edivad.dimstorage.tools.utils.InventoryUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
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
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.wrapper.InvWrapper;

public class DimTablet extends Item implements INamedContainerProvider {

	public DimTablet()
	{
		super(new Properties().group(ModSetup.dimStorageTab).maxStackSize(1));
	}

	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
	{
		World world = context.getWorld();
		PlayerEntity player = context.getPlayer();
		BlockPos pos = context.getPos();

		if(!world.isRemote)
		{
			ItemStack device = player.getHeldItem(context.getHand());
			TileEntity tile = world.getTileEntity(pos);
			if(player.isSneaking())
			{
				if(tile instanceof TileEntityDimChest)
				{
					TileEntityDimChest dimChest = (TileEntityDimChest) tile;
					if(dimChest.canAccess(player))
					{
						CompoundNBT tag = new CompoundNBT();
						tag.put("frequency", dimChest.frequency.writeToNBT(new CompoundNBT()));
						tag.putBoolean("bound", true);
						tag.putBoolean("autocollect", false);
						device.setTag(tag);

						player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Linked to chest"));
						return ActionResultType.SUCCESS;
					}
					player.sendMessage(new StringTextComponent(TextFormatting.RED + "Access Denied!"));
				}
				else
				{
					stack.getTag().putBoolean("autocollect", !stack.getTag().getBoolean("autocollect"));
					if(stack.getTag().getBoolean("autocollect"))
						player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Enabled autocollect"));
					else
						player.sendMessage(new StringTextComponent(TextFormatting.RED + "Disabled autocollect"));
				}
			}
		}
		return ActionResultType.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		if(!world.isRemote && hand.compareTo(Hand.MAIN_HAND) == 0)
		{
			if(player.isSneaking())
				return super.onItemRightClick(world, player, hand);
			if(!stack.getOrCreateTag().getBoolean("bound"))
			{
				player.sendMessage(new StringTextComponent(TextFormatting.RED + "Dimensional Tablet not connected to any DimChest"));
				return new ActionResult<>(ActionResultType.PASS, stack);
			}
			Frequency f = new Frequency(stack.getOrCreateTag().getCompound("frequency"));
			if(!f.canAccess(player))
				return new ActionResult<>(ActionResultType.PASS, stack);

			NetworkHooks.openGui((ServerPlayerEntity) player, this);
		}
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(!worldIn.isRemote)
		{
			if(stack.getOrCreateTag().getBoolean("autocollect") && stack.getOrCreateTag().getBoolean("bound"))
			{
				if(entityIn instanceof PlayerEntity)
				{
					PlayerEntity pe = (PlayerEntity) entityIn;
					Frequency f = new Frequency(stack.getOrCreateTag().getCompound("frequency"));
					InvWrapper chestInventory = new InvWrapper(getStorage(worldIn, f));

					for(int i = 0; i < pe.inventory.getSizeInventory(); i++)
					{
						Item item = pe.inventory.getStackInSlot(i).getItem();
						if(Config.DIMTABLET_LIST.get().contains(item.getRegistryName().toString()))
						{
							InventoryUtils.mergeItemStack(pe.inventory.getStackInSlot(i), 0, getStorage(worldIn, f).getSizeInventory(), chestInventory);
						}
					}
				}
			}
		}
	}

	private DimChestStorage getStorage(World world, Frequency frequency)
	{
		return (DimChestStorage) DimStorageManager.instance(world.isRemote).getStorage(frequency, "item");
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(worldIn != null)
		{
			if(!stack.hasTag() || !stack.getTag().getBoolean("bound"))
			{
				tooltip.add(new StringTextComponent(Translate.translateToLocal("message." + Main.MODID + ".adviceToLink")));
				return;
			}

			CompoundNBT tag = stack.getTag();
			if(Screen.hasShiftDown())
			{
				Frequency f = new Frequency(tag.getCompound("frequency"));
				tooltip.add(new StringTextComponent(TextFormatting.GRAY + Translate.translateToLocal("gui." + Main.MODID + ".frequency") + " " + f.getChannel()));
				if(f.hasOwner())
					tooltip.add(new StringTextComponent(TextFormatting.GRAY + Translate.translateToLocal("gui." + Main.MODID + ".owner") + " " + f.getOwner()));

				String yes = Translate.translateToLocal("gui." + Main.MODID + ".yes");
				String no = Translate.translateToLocal("gui." + Main.MODID + ".no");
				tooltip.add(new StringTextComponent(TextFormatting.GRAY + Translate.translateToLocal("gui." + Main.MODID + ".collecting") + ": " + (tag.getBoolean("autocollect") ? yes : no)));
			}
			else
				tooltip.add(new StringTextComponent(Translate.translateToLocal("message." + Main.MODID + ".holdShift")));

			tooltip.add(new StringTextComponent(Translate.translateToLocal("message." + Main.MODID + ".changeAutoCollect")));
		}
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity)
	{
		return new ContainerDimTablet(id, playerInventory, playerEntity.world);
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent(this.getTranslationKey());
	}
}