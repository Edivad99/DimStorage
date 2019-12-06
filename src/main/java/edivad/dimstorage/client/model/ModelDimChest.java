package edivad.dimstorage.client.model;

import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelDimChest extends ModelBase {

	private TileEntityDimChest tileEntity;

	private ModelRenderer base;
	private ModelRenderer top1;
	private ModelRenderer top2;
	private ModelRenderer top3;
	private ModelRenderer top4;
	private ModelRenderer top5;
	private ModelRenderer movable;
	private ModelRenderer indicatorGreen, indicatorBlue, indicatorRed;

	public ModelDimChest()
	{
		this.textureWidth = 128;
		this.textureHeight = 128;

		this.base = new ModelRenderer(this, 0, 0);
		this.base.addBox(0F, 0F, 0F, 16, 13, 16);
		this.base.setRotationPoint(-8F, 11F, -8F);
		this.base.setTextureSize(128, 128);
		this.base.mirror = true;
		this.setRotation(this.base, 0F, 0F, 0F);

		this.top1 = new ModelRenderer(this, 66, 2);
		this.top1.addBox(0F, 0F, 0F, 16, 3, 2);
		this.top1.setRotationPoint(-8F, 8F, -8F);
		this.top1.setTextureSize(128, 128);
		this.top1.mirror = true;
		this.setRotation(this.top1, 0F, 0F, 0F);

		this.top2 = new ModelRenderer(this, 0, 32);
		this.top2.addBox(0F, 0F, 0F, 2, 3, 14);
		this.top2.setRotationPoint(6F, 8F, -6F);
		this.top2.setTextureSize(128, 128);
		this.top2.mirror = true;
		this.setRotation(this.top2, 0F, 0F, 0F);

		this.top3 = new ModelRenderer(this, 36, 32);
		this.top3.addBox(0F, 0F, 0F, 2, 3, 14);
		this.top3.setRotationPoint(-8F, 8F, -6F);
		this.top3.setTextureSize(128, 128);
		this.top3.mirror = true;
		this.setRotation(this.top3, 0F, 0F, 0F);

		this.top4 = new ModelRenderer(this, 66, 10);
		this.top4.addBox(0F, 0F, 0F, 12, 3, 2);
		this.top4.setRotationPoint(-6F, 8F, 6F);
		this.top4.setTextureSize(128, 128);
		this.top4.mirror = true;
		this.setRotation(this.top4, 0F, 0F, 0F);

		this.top5 = new ModelRenderer(this, 72, 32);
		this.top5.addBox(0F, 0F, 0F, 12, 2, 6);
		this.top5.setRotationPoint(-6F, 8F, 0F);
		this.top5.setTextureSize(128, 128);
		this.top5.mirror = true;
		this.setRotation(this.top5, 0F, 0F, 0F);

		this.movable = new ModelRenderer(this, 70, 24);
		this.movable.addBox(0F, 0F, 0F, 12, 1, 6);
		this.movable.setRotationPoint(-6F, 8.533334F, -6F);
		this.movable.setTextureSize(128, 128);
		this.movable.mirror = true;
		this.setRotation(this.movable, 0F, 0F, 0F);

		this.indicatorGreen = this.createIndicator(0);
		this.indicatorBlue = this.createIndicator(2);
		this.indicatorRed = this.createIndicator(4);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		// render boxes
		base.render(f5);
		top1.render(f5);
		top2.render(f5);
		top3.render(f5);
		top4.render(f5);
		top5.render(f5);

		// render movable part
		this.movable.offsetZ = this.tileEntity.movablePartState;
		this.movable.render(f5);

		// check state
		if(this.tileEntity.locked)
			this.indicatorRed.render(f5);
		else if(this.tileEntity.frequency.hasOwner())
			this.indicatorBlue.render(f5);
		else
			this.indicatorGreen.render(f5);
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