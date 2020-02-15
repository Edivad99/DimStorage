package edivad.dimstorage.items;

import java.util.List;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.container.ContainerDimTablet;
import edivad.dimstorage.setup.ModSetup;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tools.Message;
import edivad.dimstorage.tools.Message.Messages;
import edivad.dimstorage.tools.Translate;
import net.minecraft.client.util.ITooltipFlag;
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

		if(world.isRemote)
			return ActionResultType.PASS;

		ItemStack device = player.getHeldItem(context.getHand());
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityDimChest && player.isSneaking())
		{
			TileEntityDimChest dimChest = (TileEntityDimChest) tile;
			if(dimChest.canAccess(player))
			{
				CompoundNBT tag = new CompoundNBT();
				tag.put("frequency", dimChest.frequency.writeToNBT(new CompoundNBT()));
				tag.putBoolean("bound", true);
				device.setTag(tag);

				Message.sendChatMessage(player, Messages.BINDINGCOMPLETE);
				return ActionResultType.SUCCESS;
			}
			Message.sendChatMessage(player, Messages.ACCESSDENIED);
		}
		return ActionResultType.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		if(!world.isRemote)
		{
			if(player.isSneaking())
				return super.onItemRightClick(world, player, hand);
			if(!stack.getOrCreateTag().getBoolean("bound"))
			{
				Message.sendChatMessage(player, Messages.NOTLINKED);
				return new ActionResult<>(ActionResultType.PASS, stack);
			}

			Frequency f = new Frequency(stack.getOrCreateTag().getCompound("frequency"));
			if(!f.canAccess(player))
				return new ActionResult<>(ActionResultType.PASS, stack);

			NetworkHooks.openGui((ServerPlayerEntity) player, this);
		}
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(worldIn != null)
		{
			if(!stack.hasTag() || !stack.getTag().getBoolean("bound"))
			{
				tooltip.add(new StringTextComponent(TextFormatting.GRAY + Translate.translateToLocal("message." + Main.MODID + ".adviceToLink")));
				return;
			}

			CompoundNBT tag = stack.getTag();
			Frequency f = new Frequency(tag.getCompound("frequency"));
			tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Freq: " + f.getChannel()));
			if(f.hasOwner())
				tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Owner: " + f.getOwner()));
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