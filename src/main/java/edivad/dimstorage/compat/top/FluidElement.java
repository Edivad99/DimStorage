package edivad.dimstorage.compat.top;

import edivad.dimstorage.Main;
import edivad.dimstorage.tile.TileEntityDimTank;
import edivad.dimstorage.tools.utils.FluidUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class FluidElement extends TOPElement {

    public static final ResourceLocation ID = new ResourceLocation(Main.MODID, "fluid_element");

    protected final FluidStack fluid;
    protected final int capacity;
    protected final int colorLiquid;

    protected FluidElement(FluidStack fluid, int capacity, int colorLiquid)
    {
        super(0xFF000000, 0xFFFFFF);
        this.fluid = fluid;
        this.capacity = capacity;
        this.colorLiquid = colorLiquid;
    }

    public FluidElement(TileEntityDimTank tile, int capacity)
    {
        this(tile.liquidState.serverLiquid, capacity, FluidUtils.getLiquidColorWithBiome(tile.liquidState.serverLiquid, tile));
    }

    public FluidElement(FriendlyByteBuf buf)
    {
        this(buf.readFluidStack(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeFluidStack(fluid);
        buf.writeInt(capacity);
        buf.writeInt(colorLiquid);
    }

    @Override
    public int getScaledLevel(int level)
    {
        if(capacity == 0 || fluid.getAmount() == Integer.MAX_VALUE)
        {
            return level;
        }
        return fluid.getAmount() * level / capacity;
    }

    @Override
    public TextureAtlasSprite getIcon()
    {
        return fluid.isEmpty() ? null : FluidUtils.getFluidTexture(fluid);
    }

    @Override
    public TextComponent getText()
    {
        String liquidText = fluid.getDisplayName().getString();
        int amount = fluid.getAmount();
        return new TextComponent(String.format("%s: %dmB", liquidText, amount));
    }

    @Override
    protected boolean applyRenderColor()
    {
        FluidUtils.color(colorLiquid);
        return true;
    }

    @Override
    public ResourceLocation getID()
    {
        return ID;
    }
}
