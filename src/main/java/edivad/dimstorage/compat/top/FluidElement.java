package edivad.dimstorage.compat.top;

import javax.annotation.Nonnull;

import edivad.dimstorage.tools.extra.fluid.FluidUtils;
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

	public FluidElement(@Nonnull FluidStack fluid, int capacity)
	{
		super(0xFF000000, 0xFFFFFF);
		this.fluid = fluid;
		this.capacity = capacity;
	}

	public FluidElement(ByteBuf buf)
	{
		this(new PacketBuffer(buf).readFluidStack(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		new PacketBuffer(buf).writeFluidStack(fluid);
		buf.writeInt(capacity);
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
		FluidUtils.color(fluid);
		return true;
	}

	@Override
	public int getID()
	{
		return ID;
	}
}
