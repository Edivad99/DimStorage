package edivad.dimstorage.compat.top;

import javax.annotation.Nonnull;

import edivad.dimstorage.tile.TileEntityDimTank;
import edivad.dimstorage.tools.utils.FluidUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fluids.FluidStack;

public class FluidElement extends TOPElement {

	public static int ID;

	@Nonnull
	protected final FluidStack fluid;
	protected final int capacity;
	protected final int colorLiquid;

	protected FluidElement(@Nonnull FluidStack fluid, int capacity, int colorLiquid)
	{
		super(0xFF000000, 0xFFFFFF);
		this.fluid = fluid;
		this.capacity = capacity;
		this.colorLiquid = colorLiquid;
	}

	public FluidElement(@Nonnull TileEntityDimTank tile, int capacity)
	{
		this(tile.liquidState.serverLiquid, capacity, FluidUtils.getLiquidColorWithBiome(tile.liquidState.serverLiquid, tile));
	}

	public FluidElement(ByteBuf buf)
	{
		this(new PacketBuffer(buf).readFluidStack(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		new PacketBuffer(buf).writeFluidStack(fluid);
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
	public ITextComponent getText()
	{
		String liquidText = fluid.getDisplayName().getFormattedText();
		int amount = fluid.getAmount();
		return new StringTextComponent(String.format("%s: %dmB", liquidText, amount));
	}

	@Override
	protected boolean applyRenderColor()
	{
		FluidUtils.color(colorLiquid);
		return true;
	}

	@Override
	public int getID()
	{
		return ID;
	}
}
