package edivad.dimstorage.tools.extra.fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public class FluidUtils {

	public static FluidStack read(CompoundNBT tag)
	{
		FluidStack stack = FluidStack.loadFluidStackFromNBT(tag);
		return stack == null ? FluidStack.EMPTY : stack;
	}

	public static CompoundNBT write(FluidStack fluid, CompoundNBT tag)
	{
		return fluid == null || fluid.getFluid() == null ? tag : fluid.writeToNBT(tag);
	}

	public static int getLuminosity(FluidStack stack, double density)
	{
		Fluid fluid = stack.getFluid();
		if(fluid == null)
		{
			return 0;
		}
		int light = fluid.getAttributes().getLuminosity(stack);
		if(fluid.getAttributes().isGaseous())
		{
			light = (int) (light * density);
		}
		return light;
	}

	//Render liquid
	public static float getRed(int color)
	{
		return (color >> 16 & 0xFF) / 255.0F;
	}

	public static float getGreen(int color)
	{
		return (color >> 8 & 0xFF) / 255.0F;
	}

	public static float getBlue(int color)
	{
		return (color & 0xFF) / 255.0F;
	}

	public static float getAlpha(int color)
	{
		return (color >> 24 & 0xFF) / 255.0F;
	}

	public static void color(int color)
	{
		RenderSystem.color4f(getRed(color), getGreen(color), getBlue(color), getAlpha(color));
	}

	public static void color(@Nonnull FluidStack fluid)
	{
		if(!fluid.isEmpty())
		{
			color(fluid.getFluid().getAttributes().getColor(fluid));
		}
	}

	@Nullable
	public static TextureAtlasSprite getFluidTexture(FluidStack stack)
	{
		FluidAttributes fa = stack.getFluid().getAttributes();
		ResourceLocation still = fa.getStillTexture(stack);
		return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(still);
	}
}
