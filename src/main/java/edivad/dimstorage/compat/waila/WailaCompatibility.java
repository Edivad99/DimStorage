package edivad.dimstorage.compat.waila;

import java.util.List;

import org.apache.logging.log4j.Level;

import edivad.dimstorage.Main;
import edivad.dimstorage.blocks.DimChest;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class WailaCompatibility implements IWailaDataProvider {

	public static final WailaCompatibility INSTANCE = new WailaCompatibility();

	private static boolean registered;
	private static boolean loaded;

	public static void load(IWailaRegistrar registrar)
	{
		if(!registered)
		{
			throw new RuntimeException("Please register this handler using the provided method.");
		}

		if(!loaded)
		{
			registrar.registerBodyProvider(INSTANCE, DimChest.class);
			Main.logger.log(Level.INFO, "Enabled support for Waila");
			loaded = true;
		}
	}

	public static void register()
	{
		if(registered)
			return;

		FMLInterModComms.sendMessage("waila", "register", "edivad.dimstorage.compat.waila.WailaCompatibility.load");
		registered = true;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos)
	{
		return tag;
	}

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		Block block = accessor.getBlock();
		if(block instanceof WailaInfoProvider)
		{
			return ((WailaInfoProvider) block).getWailaBody(itemStack, currenttip, accessor, config);
		}
		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}

}
