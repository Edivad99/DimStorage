package edivad.dimstorage.client.render.tile;

import edivad.dimstorage.Main;
import edivad.dimstorage.ModBlocks;
import edivad.dimstorage.client.model.ModelDimChest;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RenderTileDimChest extends TileEntitySpecialRenderer<TileEntityDimChest> {

	private static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/models/dimchest.png");

	private ModelDimChest model;

	public RenderTileDimChest()
	{
		this.model = new ModelDimChest();
	}

	@Override
	public void render(TileEntityDimChest te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te == null || te.isInvalid())
			return;

		super.render(te, x, y, z, partialTicks, destroyStage, alpha);

		GlStateManager.pushMatrix();

		GlStateManager.translate(x, y, z);

		TileEntityDimChest myTileEntity = (TileEntityDimChest) te;
		this.renderBlock(myTileEntity, myTileEntity.getWorld(), myTileEntity.getPos(), ModBlocks.dimChest);
		GlStateManager.popMatrix();
	}

	private void renderBlock(TileEntityDimChest tileEntity, World world, BlockPos pos, Block block)
	{

		int rot = 0;
		if(tileEntity != null)
			rot = tileEntity.rotation;

		GlStateManager.pushMatrix();
		this.bindTexture(texture);

		GlStateManager.translate(0.5F, -0.5F, 0.5F);
		//This line actually rotates the renderer.

		/** direction **/
		switch (rot)
		{
			case 0:
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				break;
			case 1:
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				break;
			case 2:
				GlStateManager.rotate(0F, 0F, 1F, 0F);
				break;
			case 3:
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				break;
		}
		GlStateManager.rotate(180F, 0F, 1F, 0F);

		/** sens **/
		GlStateManager.rotate(180F, 1F, 0F, 0F);

		/** Ajustement **/
		GlStateManager.translate(0F, -2F, 0F);

		this.model.setTileEntity(tileEntity);
		this.model.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		GlStateManager.popMatrix();
	}

}
