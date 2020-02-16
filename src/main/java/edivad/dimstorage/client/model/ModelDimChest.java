package edivad.dimstorage.client.model;

import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class ModelDimChest extends Model {

	private TileEntityDimChest tileEntity;

	private RendererModel base;
	private RendererModel top1;
	private RendererModel top2;
	private RendererModel top3;
	private RendererModel top4;
	private RendererModel top5;
	private RendererModel movable;
	private RendererModel indicatorGreen, indicatorBlue, indicatorRed, indicatorYellow;

	public ModelDimChest()
	{
		this.textureWidth = 128;
		this.textureHeight = 128;

		base = new RendererModel(this, 0, 0);
		base.addBox(0F, 0F, 0F, 16, 13, 16);
		base.setRotationPoint(-8F, 11F, -8F);
		base.setTextureSize(128, 128);
		base.mirror = true;
		setRotation(this.base, 0F, 0F, 0F);

		top1 = new RendererModel(this, 66, 2);
		top1.addBox(0F, 0F, 0F, 16, 3, 2);
		top1.setRotationPoint(-8F, 8F, -8F);
		top1.setTextureSize(128, 128);
		top1.mirror = true;
		setRotation(this.top1, 0F, 0F, 0F);

		top2 = new RendererModel(this, 0, 32);
		top2.addBox(0F, 0F, 0F, 2, 3, 14);
		top2.setRotationPoint(6F, 8F, -6F);
		top2.setTextureSize(128, 128);
		top2.mirror = true;
		setRotation(this.top2, 0F, 0F, 0F);

		top3 = new RendererModel(this, 36, 32);
		top3.addBox(0F, 0F, 0F, 2, 3, 14);
		top3.setRotationPoint(-8F, 8F, -6F);
		top3.setTextureSize(128, 128);
		top3.mirror = true;
		setRotation(this.top3, 0F, 0F, 0F);

		top4 = new RendererModel(this, 66, 10);
		top4.addBox(0F, 0F, 0F, 12, 3, 2);
		top4.setRotationPoint(-6F, 8F, 6F);
		top4.setTextureSize(128, 128);
		top4.mirror = true;
		setRotation(this.top4, 0F, 0F, 0F);

		top5 = new RendererModel(this, 72, 32);
		top5.addBox(0F, 0F, 0F, 12, 2, 6);
		top5.setRotationPoint(-6F, 8F, 0F);
		top5.setTextureSize(128, 128);
		top5.mirror = true;
		setRotation(this.top5, 0F, 0F, 0F);

		movable = new RendererModel(this, 70, 24);
		movable.addBox(0F, 0F, 0F, 12, 1, 6);
		movable.setRotationPoint(-6F, 8.533334F, -6F);
		movable.setTextureSize(128, 128);
		movable.mirror = true;
		setRotation(this.movable, 0F, 0F, 0F);

		indicatorGreen = this.createIndicator(0);
		indicatorBlue = this.createIndicator(2);
		indicatorRed = this.createIndicator(4);
		indicatorYellow = this.createIndicator(6);
	}

	public void render(float f)
	{
		// render boxes
		base.render(f);
		top1.render(f);
		top2.render(f);
		top3.render(f);
		top4.render(f);
		top5.render(f);

		// render movable part
		movable.offsetZ = tileEntity.movablePartState;
		movable.render(f);

		// check state
		if(tileEntity.locked)
			indicatorRed.render(f);
		else if(tileEntity.frequency.hasOwner())
			indicatorBlue.render(f);
		else if(tileEntity.collect)
			indicatorYellow.render(f);
		else
			indicatorGreen.render(f);
	}

	private RendererModel createIndicator(int offsetY)
	{
		RendererModel indicator = new RendererModel(this, 0, offsetY);
		indicator.addBox(0F, 0F, 0F, 2, 1, 1);
		indicator.setRotationPoint(-5F, 7.5F, 4F);
		indicator.setTextureSize(128, 128);
		indicator.mirror = true;
		setRotation(indicator, 0F, 0F, 0F);

		return indicator;
	}

	private void setRotation(RendererModel model, float x, float y, float z)
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