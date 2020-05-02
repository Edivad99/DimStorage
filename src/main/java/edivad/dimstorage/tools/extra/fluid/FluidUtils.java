package edivad.dimstorage.tools.extra.fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
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

	@Nullable
	public static TextureAtlasSprite getFluidTexture(@Nonnull FluidStack stack)
	{
		FluidAttributes fa = stack.getFluid().getAttributes();
		ResourceLocation still = fa.getStillTexture(stack);
		return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(still);
	}
	
	public static int getLiquidColorWithBiome(@Nonnull FluidStack fluid, @Nonnull World world, @Nonnull BlockPos pos)
	{
		int color;
		if(fluid.isFluidEqual(new FluidStack(Fluids.WATER, 1000)))
			color = BiomeColors.getWaterColor(world, pos) | 0xFF000000;
		else
			color = fluid.getFluid().getAttributes().getColor(fluid);
		return color;
	}
	
	public static int getLiquidColorWithBiome(@Nonnull FluidStack fluid, @Nonnull TileEntity tileEntity)
	{
		return getLiquidColorWithBiome(fluid, tileEntity.getWorld(), tileEntity.getPos());
	}
}
