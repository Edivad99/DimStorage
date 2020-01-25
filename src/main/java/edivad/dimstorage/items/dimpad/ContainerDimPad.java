package edivad.dimstorage.items.dimpad;

import edivad.dimstorage.Main;
import edivad.dimstorage.ModBlocks;
import edivad.dimstorage.container.ContainerDimChest;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tools.Message;
import edivad.dimstorage.tools.Message.Messages;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class ContainerDimPad extends ContainerDimChest {

	public ContainerDimPad(int windowId, PlayerInventory playerInventory)
	{
		super(ModBlocks.containerDimPad, windowId, playerInventory, getTile(playerInventory.player), false);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return true;
	}
	
	private static TileEntityDimChest getTile(PlayerEntity playerEntity)
	{
		ItemStack stack = playerEntity.getHeldItem(Hand.MAIN_HAND);
		BlockPos pos = DimPad.readBlockPosFromStack(stack);
		
		
//		int dim = stack.getTag().getInt("dim");
//		System.out.println("------------------------>>>>>>>>>>>>>>>>> " + dim);
//		World serverWorld = Main.getServer().func_71218_a(DimensionType.getById(dim)).getWorld();
//		
//		if(!serverWorld.isAreaLoaded(pos, 1))
//			Message.sendChatMessage(playerEntity, Messages.AREANOTLOADED);
//		
//		return (TileEntityDimChest) serverWorld.getTileEntity(pos);
		return (TileEntityDimChest) playerEntity.world.getTileEntity(pos);
	}
}
