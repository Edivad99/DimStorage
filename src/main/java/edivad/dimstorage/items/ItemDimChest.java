package edivad.dimstorage.items;

import java.util.List;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blocks.DimChest;
import edivad.dimstorage.tile.TileFrequencyOwner;
import edivad.dimstorage.tools.Translate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemDimChest extends BlockItem {

	public ItemDimChest(Block block)
	{
		super(block, new Item.Properties().group(Main.dimStorageTab).maxStackSize(1));
		setRegistryName(DimChest.DIMCHEST);
	}

	public Frequency getFreq(ItemStack stack)
	{
		return Frequency.readFromStack(stack);
	}
	
	@Override
	protected boolean placeBlock(BlockItemUseContext context, BlockState state)
	{
		if(super.placeBlock(context, state))
		{
			World world = context.getWorld();
			BlockPos pos = context.getPos();
			ItemStack stack = context.getItem();
			
			TileFrequencyOwner tile = (TileFrequencyOwner) world.getTileEntity(pos);
			tile.setFreq(getFreq(stack));
			return true;
		}
		return false;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		Frequency frequency = Frequency.readFromStack(stack);
		if(frequency.hasOwner())
			tooltip.add(new StringTextComponent(TextFormatting.DARK_RED + Translate.translateToLocal("gui." + Main.MODID + ".owner") + " " + frequency.getOwner()));
		if(stack.hasTag())
			tooltip.add(new StringTextComponent(Translate.translateToLocal("gui." + Main.MODID + ".frequency") + " " + frequency.getChannel()));
		
	}
}
