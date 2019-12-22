package edivad.dimstorage.proxy;

import javax.annotation.Nullable;

import edivad.dimstorage.client.gui.GuiDimChest;
import edivad.dimstorage.container.ContainerDimChest;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler {

	@Nullable
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		int block = ID / 2;

		if(block == 0)//DimChest
		{
			if(te instanceof TileEntityDimChest)
			{
				TileEntityDimChest tile = (TileEntityDimChest) te;
				return new ContainerDimChest(player.inventory, ((TileEntityDimChest) te).getStorage());
			}
		}
		return null;
	}

	@Nullable
	@Override
	@SideOnly(Side.CLIENT)
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);

		int block = ID / 2;
		boolean openGUI = (ID % 2) != 0;

		if(block == 0)//DimChest
		{
			if(te instanceof TileEntityDimChest)
			{
				TileEntityDimChest tile = (TileEntityDimChest) te;
				tile.getStorage().empty();
				return new GuiDimChest(player.inventory, tile.getStorage(), tile, openGUI);
			}
		}

		return null;
	}
}
