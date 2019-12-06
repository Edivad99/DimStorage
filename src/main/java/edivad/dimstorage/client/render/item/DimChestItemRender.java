package edivad.dimstorage.client.render.item;

import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.TransformUtils;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.client.model.ModelDimChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

public class DimChestItemRender implements IItemRenderer {

	private final ResourceLocation texture = new ResourceLocation("dimstorage", "textures/models/dimchest.png");

	private ModelDimChest model = new ModelDimChest();

	@Override
	public void renderItem(ItemStack item, TransformType transformType)
	{
		GlStateManager.pushMatrix();

		Frequency frequency = Frequency.readFromStack(item);
		//RenderTileEnderChest.renderChest(2, frequency, 0, 0, 0, 0, 0F);
		render(0, 0, 0, 0, 0, 0);

		//Fixes issues with inventory rendering.
		//The Portal renderer modifies blend and disables it.
		//Vanillas inventory relies on the fact that items don't modify gl so it never bothers to set it again.
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.popMatrix();
	}

	@Override
	public IModelState getTransforms()
	{
		return TransformUtils.DEFAULT_BLOCK;
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return false;
	}

	@Override
	public boolean isGui3d()
	{
		return false;
	}

	private void render(double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();

		GlStateManager.translate(x, y, z);

		renderBlock();
		GlStateManager.popMatrix();
	}

	private void renderBlock()
	{

		GlStateManager.pushMatrix();
		TextureUtils.changeTexture(texture);

		GlStateManager.translate(0.5F, -0.5F, 0.5F);
		//This line actually rotates the renderer.

		/** direction **/
		GlStateManager.rotate(180F, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 0F, 1F, 0F);

		/** sens **/
		GlStateManager.rotate(180F, 1F, 0F, 0F);

		/** Ajustement **/
		GlStateManager.translate(0F, -2F, 0F);

		this.model.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		GlStateManager.popMatrix();
	}
}
