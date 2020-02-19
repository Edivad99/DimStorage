package edivad.dimstorage.client.render.tile;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.model.ModelDimChest;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderTileDimChest extends TileEntityRenderer<TileEntityDimChest> {

	private ModelDimChest model;

	public RenderTileDimChest(TileEntityRendererDispatcher dispatcher)
	{
		super(dispatcher);
		model = new ModelDimChest();
	}

	@Override
	public void render(TileEntityDimChest te, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		if(te == null || te.isRemoved())
			return;

		matrixStackIn.push();
		matrixStackIn.translate(0D, 0D, 0D);
		renderBlock(te, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		matrixStackIn.pop();
	}

	private void renderBlock(@Nonnull TileEntityDimChest te, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		int rot = te.rotation;

		matrixStackIn.push();

		matrixStackIn.translate(0.5D, -0.5D, 0.5D);
		//This line actually rotates the renderer.

		/** direction **/
		matrixStackIn.rotate(new Quaternion(0F, 360 - rot * 90, 0F, true));

		/** sens **/
		matrixStackIn.rotate(new Quaternion(180F, 0F, 0F, true));

		/** Ajustement **/
		matrixStackIn.translate(0D, -2D, 0D);

		model.setTileEntity(te);

		IVertexBuilder buffer = bufferIn.getBuffer(RenderType.getEntitySolid(new ResourceLocation(Main.MODID, "textures/models/dimchest.png")));
		model.render(matrixStackIn, buffer, combinedLightIn, combinedOverlayIn, 1F, 1F, 1F, 1F);

		matrixStackIn.pop();
	}
}
