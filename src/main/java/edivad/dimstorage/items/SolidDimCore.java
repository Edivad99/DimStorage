package edivad.dimstorage.items;

import edivad.dimstorage.Main;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SolidDimCore extends Item {

	public SolidDimCore()
	{
		super();

		this.setCreativeTab(Main.tabDimStorage);
		setRegistryName(new ResourceLocation(Main.MODID, "solid_dim_core"));
		setUnlocalizedName(Main.MODID + "." + "solid_dim_core");
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
