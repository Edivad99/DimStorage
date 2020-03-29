package edivad.dimstorage.client.render.tile;

import javax.annotation.Nonnull;

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
		model = new ModelDimChest();
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

	private void renderBlock(@Nonnull TileEntityDimChest te)
	{
		int rot = te.rotation;

		GlStateManager.pushMatrix();
		this.bindTexture(texture);

		GlStateManager.translatef(0.5F, -0.5F, 0.5F);
		//This line actually rotates the renderer.

		/** direction **/
		GlStateManager.rotatef(360 - rot * 90, 0F, 1F, 0F);

		/** sens **/
		GlStateManager.rotatef(180F, 1F, 0F, 0F);

		/** Ajustement **/
		GlStateManager.translatef(0F, -2F, 0F);

		model.setTileEntity(te);
		model.render();

		GlStateManager.popMatrix();
	}
}
