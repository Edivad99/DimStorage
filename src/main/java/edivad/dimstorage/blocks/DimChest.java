package edivad.dimstorage.blocks;

import edivad.dimstorage.Main;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.client.render.tile.RenderTileDimChest;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tile.TileFrequencyOwner;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DimChest extends Block implements ITileEntityProvider {

	public static final ResourceLocation DIMCHEST = new ResourceLocation(Main.MODID, "dimensional_chest");

	public DimChest()
	{
		super(Material.ROCK);

		this.setHardness(20F);
		this.setResistance(100F);
		this.setSoundType(SoundType.STONE);

		setRegistryName(DIMCHEST);
		setUnlocalizedName(Main.MODID + "." + "dimensional_chest");

		this.setCreativeTab(Main.tabDimStorage);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return new TileEntityDimChest();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityDimChest)
		{
			TileEntityDimChest chest = (TileEntityDimChest) tile;
			if(chest.canAccess() || player.isCreative())
				return willHarvest || super.removedByPlayer(state, world, pos, player, false);
		}
		return false;
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
	{
		super.harvestBlock(worldIn, player, pos, state, te, stack);
		worldIn.setBlockToAir(pos);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		TileFrequencyOwner tile = (TileFrequencyOwner) world.getTileEntity(pos);
		if(tile != null)
		{
			drops.add(createItem(state.getBlock().getMetaFromState(state), tile.frequency));
			//            if (ConfigurationHandler.anarchyMode && tile.frequency.hasOwner()) {
			//                drops.add(ConfigurationHandler.personalItem.copy());
			//            }
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult rayTraceResult, World world, BlockPos pos, EntityPlayer player)
	{
		TileFrequencyOwner tile = (TileFrequencyOwner) world.getTileEntity(pos);
		return createItem(this.getMetaFromState(state), tile.frequency);
	}

	private ItemStack createItem(int meta, Frequency freq)
	{
		ItemStack stack = new ItemStack(this, 1, meta);
		if(!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}

		freq.writeToStack(stack);
		return stack;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.isRemote)
			return true;

		TileEntity tile = worldIn.getTileEntity(pos);
		if(!(tile instanceof TileFrequencyOwner))
			return false;

		TileFrequencyOwner owner = (TileFrequencyOwner) tile;

		return !playerIn.isSneaking() && owner.activate(playerIn, worldIn, pos);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TileFrequencyOwner)
		{
			((TileFrequencyOwner) tile).onPlaced(placer);
		}
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TileFrequencyOwner && ((TileFrequencyOwner) tile).rotate();
	}

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDimChest.class, new RenderTileDimChest());
	}
}
