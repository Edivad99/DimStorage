package edivad.dimstorage.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;

import edivad.dimstorage.Main;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import edivad.dimstorage.client.model.ModelDimChest;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.model.TransformationHelper;

@OnlyIn(Dist.CLIENT)
public class RenderTileDimChest extends TileEntityRenderer<TileEntityDimChest> {

	private static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/models/dimchest.png");

	//private ModelDimChest model;

	public RenderTileDimChest(TileEntityRendererDispatcher rendererDispatcher)
	{
		super(rendererDispatcher);
		//this.model = new ModelDimChest();
	}

	@Override
	public void render(TileEntityDimChest te, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		if(te == null || te.isRemoved())
			return;

		matrixStackIn.push();
		//matrixStackIn.translate(p_227861_1_, p_227861_3_, p_227861_5_);
		matrixStackIn.translate(0D, 0D, 0D);
		//renderBlock(te, matrixStackIn);
		matrixStackIn.pop();

	}

	//	@Override
	//	public void render(TileEntityDimChest te, double x, double y, double z, float partialTicks, int destroyStage)
	//	{
	//		if(te == null || te.isRemoved())
	//			return;
	//
	//		GlStateManager.pushMatrix();
	//		GlStateManager.translated(x, y, z);
	//		renderBlock(te);
	//		GlStateManager.popMatrix();
	//	}

	private void renderBlock(TileEntityDimChest te, MatrixStack matrix)
	{
		int rot = 0;
		if(te != null)
			rot = te.rotation;

		matrix.push();
		//this.bindTexture(texture);

		matrix.translate(0.5F, -0.5F, 0.5F);
		//This line actually rotates the renderer.

		/** direction **/
		matrix.rotate(TransformationHelper.quatFromXYZ(new Vector3f(360 - rot * 90, 0, 1), true));

		/** sens **/
		matrix.rotate(TransformationHelper.quatFromXYZ(new Vector3f(180, 1, 0), true));

		/** Ajustement **/
		matrix.translate(0F, -2F, 0F);

		//model.setTileEntity(te);
		//model.render(0.0625F);

		matrix.pop();
	}
}
