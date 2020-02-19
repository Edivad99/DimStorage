package edivad.dimstorage.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelDimChest extends Model {

	private TileEntityDimChest tileEntity;

	private ModelRenderer base;
	private ModelRenderer top1;
	private ModelRenderer top2;
	private ModelRenderer top3;
	private ModelRenderer top4;
	private ModelRenderer top5;
	private ModelRenderer movable;
	private ModelRenderer indicatorGreen, indicatorBlue, indicatorRed, indicatorYellow;

	public ModelDimChest()
	{
		super(RenderType::getEntitySolid);
		this.textureWidth = 128;
		this.textureHeight = 128;

		base = new ModelRenderer(this, 0, 0);
		base.addBox(0F, 0F, 0F, 16, 13, 16);
		base.setRotationPoint(-8F, 11F, -8F);
		base.setTextureSize(128, 128);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);

		top1 = new ModelRenderer(this, 66, 2);
		top1.addBox(0F, 0F, 0F, 16, 3, 2);
		top1.setRotationPoint(-8F, 8F, -8F);
		top1.setTextureSize(128, 128);
		top1.mirror = true;
		setRotation(top1, 0F, 0F, 0F);

		top2 = new ModelRenderer(this, 0, 32);
		top2.addBox(0F, 0F, 0F, 2, 3, 14);
		top2.setRotationPoint(6F, 8F, -6F);
		top2.setTextureSize(128, 128);
		top2.mirror = true;
		setRotation(top2, 0F, 0F, 0F);

		top3 = new ModelRenderer(this, 36, 32);
		top3.addBox(0F, 0F, 0F, 2, 3, 14);
		top3.setRotationPoint(-8F, 8F, -6F);
		top3.setTextureSize(128, 128);
		top3.mirror = true;
		setRotation(top3, 0F, 0F, 0F);

		top4 = new ModelRenderer(this, 66, 10);
		top4.addBox(0F, 0F, 0F, 12, 3, 2);
		top4.setRotationPoint(-6F, 8F, 6F);
		top4.setTextureSize(128, 128);
		top4.mirror = true;
		setRotation(top4, 0F, 0F, 0F);

		top5 = new ModelRenderer(this, 72, 32);
		top5.addBox(0F, 0F, 0F, 12, 2, 6);
		top5.setRotationPoint(-6F, 8F, 0F);
		top5.setTextureSize(128, 128);
		top5.mirror = true;
		setRotation(top5, 0F, 0F, 0F);

		movable = new ModelRenderer(this, 70, 24);
		movable.addBox(0F, 0F, 0F, 12, 1, 6);
		movable.setRotationPoint(-6F, 8.533334F, -6F);
		movable.setTextureSize(128, 128);
		movable.mirror = true;
		setRotation(movable, 0F, 0F, 0F);

		indicatorGreen = createIndicator(0);
		indicatorBlue = createIndicator(2);
		indicatorRed = createIndicator(4);
		indicatorYellow = createIndicator(6);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		// render boxes
		base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		top1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		top2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		top3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		top4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		top5.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);

		// render movable part
		matrixStackIn.push();
		matrixStackIn.translate(0, 0, tileEntity.movablePartState);
		movable.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		matrixStackIn.pop();

		// check state
		if(tileEntity.locked)
			indicatorRed.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		else if(tileEntity.frequency.hasOwner())
			indicatorBlue.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		else if(tileEntity.collect)
			indicatorYellow.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		else
			indicatorGreen.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	private ModelRenderer createIndicator(int offsetY)
	{
		ModelRenderer indicator = new ModelRenderer(this, 0, offsetY);
		indicator.addBox(0F, 0F, 0F, 2, 1, 1);
		indicator.setRotationPoint(-5F, 7.5F, 4F);
		indicator.setTextureSize(128, 128);
		indicator.mirror = true;
		setRotation(indicator, 0F, 0F, 0F);

		return indicator;
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setTileEntity(TileEntityDimChest tileEntity)
	{
		this.tileEntity = tileEntity;
	}
}