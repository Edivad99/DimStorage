package edivad.dimstorage.tools.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public class FluidUtils {

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
		GlStateManager.color4f(getRed(color), getGreen(color), getBlue(color), getAlpha(color));
	}

	@Nullable
	public static TextureAtlasSprite getFluidTexture(@Nonnull FluidStack stack)
	{
		FluidAttributes fa = stack.getFluid().getAttributes();
		ResourceLocation still = fa.getStillTexture();
		return Minecraft.getInstance().getTextureMap().getSprite(still);
	}
	
	public static int getLiquidColorWithBiome(@Nonnull FluidStack fluid, World world, BlockPos pos)
	{
		if(fluid.isFluidEqual(new FluidStack(Fluids.WATER, 1000)))
			return BiomeColors.getWaterColor(world, pos) | 0xFF000000;
		
		return fluid.getFluid().getAttributes().getColor(fluid);
	}
	
	public static int getLiquidColorWithBiome(@Nonnull FluidStack fluid, @Nonnull TileEntity tileEntity)
	{
		return getLiquidColorWithBiome(fluid, tileEntity.getWorld(), tileEntity.getPos());
	}
}
