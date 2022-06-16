package edivad.dimstorage.tools.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.IFluidTypeRenderProperties;
import net.minecraftforge.client.RenderProperties;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidUtils {

    //Render liquid
    public static float getRed(int color) {
        return (color >> 16 & 0xFF) / 255.0F;
    }

    public static float getGreen(int color) {
        return (color >> 8 & 0xFF) / 255.0F;
    }

    public static float getBlue(int color) {
        return (color & 0xFF) / 255.0F;
    }

    public static float getAlpha(int color) {
        return (color >> 24 & 0xFF) / 255.0F;
    }

    public static void color(int color) {
        RenderSystem.setShaderColor(getRed(color), getGreen(color), getBlue(color), getAlpha(color));
    }

    @Nullable
    public static TextureAtlasSprite getFluidTexture(@Nonnull FluidStack fluidStack) {
        IFluidTypeRenderProperties properties = RenderProperties.get(fluidStack.getFluid());
        ResourceLocation still = properties.getStillTexture(fluidStack);
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(still);
    }

    public static int getLiquidColorWithBiome(@Nonnull FluidStack fluidStack, Level level, BlockPos pos) {
        if(level.isClientSide)
            if(fluidStack.getFluid().isSame(Fluids.WATER))
                return BiomeColors.getAverageWaterColor(level, pos) | 0xFF000000;

        return RenderProperties.get(fluidStack.getFluid()).getColorTint(fluidStack);
    }

    public static int getLiquidColorWithBiome(@Nonnull FluidStack fluid, @Nonnull BlockEntity tileEntity) {
        return getLiquidColorWithBiome(fluid, tileEntity.getLevel(), tileEntity.getBlockPos());
    }
}
