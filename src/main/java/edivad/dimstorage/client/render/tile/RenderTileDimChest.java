package edivad.dimstorage.client.render.tile;

import com.mojang.blaze3d.platform.GlStateManager;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.model.ModelDimChest;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderTileDimChest extends TileEntityRenderer<TileEntityDimChest> {

	private static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/models/dimchest.png");

	private ModelDimChest model;

	public RenderTileDimChest()
	{
		this.model = new ModelDimChest();
	}

	@Override
	public void render(TileEntityDimChest te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		if(te == null || te.isRemoved())
			return;

		super.render(te, x, y, z, partialTicks, destroyStage);
		
		GlStateManager.pushMatrix();
		GlStateManager.translated(x, y, z);
		renderBlock(te);
		GlStateManager.popMatrix();
	}

	private void renderBlock(TileEntityDimChest te)
	{
		int rot = 0;
		if(te != null)
			rot = te.rotation;

		GlStateManager.pushMatrix();
		this.bindTexture(texture);

		GlStateManager.translatef(0.5F, -0.5F, 0.5F);
		//This line actually rotates the renderer.

		/** direction **/
		switch (rot)
		{
			case 0:
				GlStateManager.rotatef(180F, 0F, 1F, 0F);
				break;
			case 1:
				GlStateManager.rotatef(90F, 0F, 1F, 0F);
				break;
			case 2:
				GlStateManager.rotatef(0F, 0F, 1F, 0F);
				break;
			case 3:
				GlStateManager.rotatef(270F, 0F, 1F, 0F);
				break;
		}
		GlStateManager.rotatef(180F, 0F, 1F, 0F);

		/** sens **/
		GlStateManager.rotatef(180F, 1F, 0F, 0F);

		/** Ajustement **/
		GlStateManager.translatef(0F, -2F, 0F);

		this.model.setTileEntity(te);
		this.model.render(0.0625F);

		GlStateManager.popMatrix();
	}
}
