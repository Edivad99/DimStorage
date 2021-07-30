package edivad.dimstorage.tools.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.client.renderer.BiomeColors;
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
        RenderSystem.setShaderColor(getRed(color), getGreen(color), getBlue(color), getAlpha(color));
    }

    @Nullable
    public static TextureAtlasSprite getFluidTexture(@Nonnull FluidStack stack)
    {
        FluidAttributes fa = stack.getFluid().getAttributes();
        ResourceLocation still = fa.getStillTexture(stack);
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(still);
    }

    public static int getLiquidColorWithBiome(@Nonnull FluidStack fluid, Level world, BlockPos pos)
    {
        if(world.isClientSide)
            if(fluid.isFluidEqual(new FluidStack(Fluids.WATER, 1000)))
                return BiomeColors.getAverageWaterColor(world, pos) | 0xFF000000;

        return fluid.getFluid().getAttributes().getColor(fluid);
    }

    public static int getLiquidColorWithBiome(@Nonnull FluidStack fluid, @Nonnull BlockEntity tileEntity)
    {
        return getLiquidColorWithBiome(fluid, tileEntity.getLevel(), tileEntity.getBlockPos());
    }
}
