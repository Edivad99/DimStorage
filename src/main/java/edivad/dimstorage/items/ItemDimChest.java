package edivad.dimstorage.items;

import java.util.List;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.blocks.DimChest;
import edivad.dimstorage.tile.TileFrequencyOwner;
import edivad.dimstorage.tools.Translate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemDimChest extends ItemBlock {

	public ItemDimChest(Block block)
	{
		super(block);
		setRegistryName(DimChest.DIMCHEST);
	}

	@Override
	public int getMetadata(int stackMeta)
	{
		return stackMeta;
	}

	public Frequency getFreq(ItemStack stack)
	{
		return Frequency.readFromStack(stack);
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		if(super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState))
		{
			TileFrequencyOwner tile = (TileFrequencyOwner) world.getTileEntity(pos);
			tile.setFreq(getFreq(stack));
			return true;
		}
		return false;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		Frequency frequency = Frequency.readFromStack(stack);
		if(frequency.hasOwner())
			tooltip.add(TextFormatting.DARK_RED + Translate.translateToLocal("gui." + Main.MODID + ".owner") + " " + frequency.getOwner());
		if(stack.hasTagCompound())
			tooltip.add(Translate.translateToLocal("gui." + Main.MODID + ".frequency") + " " + frequency.getChannel());
	}
}
