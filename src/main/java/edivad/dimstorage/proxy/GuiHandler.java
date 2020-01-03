package edivad.dimstorage.proxy;

public class GuiHandler /*implements IGuiHandler*/ {
//
//	@Nullable
//	@Override
//	public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z)
//	{
//		BlockPos pos = new BlockPos(x, y, z);
//		TileEntity te = world.getTileEntity(pos);
//		int block = ID / 2;
//
//		if(block == 0)//DimChest
//		{
//			if(te instanceof TileEntityDimChest)
//			{
//				TileEntityDimChest tile = (TileEntityDimChest) te;
//				return new ContainerDimChest(player.inventory, tile.getStorage());
//			}
//		}
//		return null;
//	}
//
//	@Nullable
//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z)
//	{
//		BlockPos pos = new BlockPos(x, y, z);
//		TileEntity te = world.getTileEntity(pos);
//
//		int block = ID / 2;
//		boolean openGUI = (ID % 2) != 0;
//
//		if(block == 0)//DimChest
//		{
//			if(te instanceof TileEntityDimChest)
//			{
//				TileEntityDimChest tile = (TileEntityDimChest) te;
//				tile.getStorage().empty();
//				return new ScreenDimChest(player.inventory, tile.getStorage(), tile, openGUI);
//			}
//		}
//
//		return null;
//	}
}
