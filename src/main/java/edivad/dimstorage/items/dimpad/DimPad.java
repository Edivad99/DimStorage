package edivad.dimstorage.items.dimpad;

import java.util.List;

import edivad.dimstorage.Main;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tools.Message;
import edivad.dimstorage.tools.Message.Messages;
import edivad.dimstorage.tools.Translate;
import net.minecraft.client.gui.screen.Screen;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class DimPad extends Item implements INamedContainerProvider {

	public DimPad()
	{
		super(new Properties().group(Main.dimStorageTab).maxStackSize(1));
		setRegistryName(new ResourceLocation(Main.MODID, "dim_pad"));
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
			if(dimChest.canAccess())
			{
				CompoundNBT posDimChest = new CompoundNBT();
				posDimChest.putInt("x", pos.getX());
				posDimChest.putInt("y", pos.getY());
				posDimChest.putInt("z", pos.getZ());

				CompoundNBT tag = new CompoundNBT();
				tag.put("PosDimChest", posDimChest);
				tag.putInt("dim", world.getDimension().getType().getId());
				tag.putBoolean("bound", true);
				tag.putInt("freq", dimChest.frequency.getChannel());
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
				Message.sendChatMessage(player, Messages.NOTLINKED);

			BlockPos pos = readBlockPosFromStack(player.getHeldItem(hand));
			int dim = stack.getTag().getInt("dim");

			World serverWorld = Main.getServer().func_71218_a(DimensionType.getById(dim)).getWorld();

			if(!serverWorld.isAreaLoaded(pos, 1))
			{
				Message.sendChatMessage(player, Messages.AREANOTLOADED);
				return new ActionResult<>(ActionResultType.PASS, stack);
			}

			if(serverWorld.isBlockPresent(pos))
			{
				TileEntity tile = serverWorld.getTileEntity(pos);
				if(tile != null && tile instanceof TileEntityDimChest)
				{
					TileEntityDimChest chest = (TileEntityDimChest) tile;
					if(stack.getTag().getInt("freq") != chest.frequency.getChannel())
						stack.getTag().putInt("freq", chest.frequency.getChannel());

					if(!chest.canAccess())
						return new ActionResult<>(ActionResultType.PASS, stack);

					NetworkHooks.openGui((ServerPlayerEntity) player, this);
				}
			}
		}
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	public static BlockPos readBlockPosFromStack(ItemStack stack)
	{
		if(stack.hasTag())
		{
			CompoundNBT stackTag = stack.getTag();
			if(stackTag.contains("PosDimChest"))
			{
				int x = stackTag.getCompound("PosDimChest").getInt("x");
				int y = stackTag.getCompound("PosDimChest").getInt("y");
				int z = stackTag.getCompound("PosDimChest").getInt("z");
				return new BlockPos(x, y, z);
			}
		}
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(worldIn != null)
		{
			if(stack.hasTag())
			{
				CompoundNBT tag = stack.getTag();
				if(tag.getBoolean("bound"))
				{
					BlockPos pos = readBlockPosFromStack(stack);
					if(pos != null)
					{
						if(Screen.hasShiftDown())
						{
							tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Freq: " + tag.getInt("freq")));
							//tooltip.add(new StringTextComponent("World: " + DimensionType.getById(tag.getInt("dim")).getSuffix()));
							tooltip.add(new StringTextComponent(TextFormatting.GRAY + String.format("[X:%d Y:%d Z:%d]", pos.getX(), pos.getY(), pos.getZ())));
						}
						else
							tooltip.add(new StringTextComponent(TextFormatting.GRAY + Translate.translateToLocal("message." + Main.MODID + ".pressShift")));
					}
				}
				else
					tooltip.add(new StringTextComponent(TextFormatting.GRAY + Translate.translateToLocal("message." + Main.MODID + ".adviceToLink")));
			}
			else
				tooltip.add(new StringTextComponent(TextFormatting.GRAY + Translate.translateToLocal("message." + Main.MODID + ".adviceToLink")));
		}
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity)
	{
		return new ContainerDimPad(id, playerInventory);
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent(this.getTranslationKey());
	}
}