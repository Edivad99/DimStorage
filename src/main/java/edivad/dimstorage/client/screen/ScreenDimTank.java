package edivad.dimstorage.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import edivad.dimstorage.Main;
import edivad.dimstorage.client.screen.element.button.AutoEjectButton;
import edivad.dimstorage.client.screen.pattern.FrequencyScreen;
import edivad.dimstorage.container.ContainerDimTank;
import edivad.dimstorage.storage.DimTankStorage;
import edivad.dimstorage.blockentities.BlockEntityDimTank;
import edivad.dimstorage.tools.utils.FluidUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public class ScreenDimTank extends FrequencyScreen<ContainerDimTank> {

    private static final ResourceLocation DIMTANK_GUI = new ResourceLocation(Main.MODID, "textures/gui/dimtank.png");
    private String liquid, amount, temperature, luminosity, gaseous, empty, yes, no;

    public ScreenDimTank(ContainerDimTank container, Inventory inventory, Component text) {
        super(container, container.owner, inventory, text, DIMTANK_GUI, container.isOpen);
    }

    @Override
    protected void init() {
        super.init();

        addComponent(new AutoEjectButton(width / 2 + 95, height / 2 + 75, (BlockEntityDimTank) tileOwner));

        drawSettings(drawSettings);

        liquid = new TranslatableComponent("gui." + Main.MODID + ".liquid").getString();
        amount = new TranslatableComponent("gui." + Main.MODID + ".amount").getString();
        temperature = new TranslatableComponent("gui." + Main.MODID + ".temperature").getString();
        luminosity = new TranslatableComponent("gui." + Main.MODID + ".luminosity").getString();
        gaseous = new TranslatableComponent("gui." + Main.MODID + ".gas").getString();
        empty = new TranslatableComponent("gui." + Main.MODID + ".empty").getString();
        yes = new TranslatableComponent("gui." + Main.MODID + ".yes").getString();
        no = new TranslatableComponent("gui." + Main.MODID + ".no").getString();
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        super.renderLabels(poseStack, mouseX, mouseY);
        FluidStack liquidStack = ((BlockEntityDimTank) tileOwner).liquidState.clientLiquid;

        if(!liquidStack.isEmpty()) {
            FluidAttributes liquidAttributes = liquidStack.getFluid().getAttributes();
            String liquidName = liquidStack.getDisplayName().getString();
            this.font.draw(poseStack, liquid + " " + liquidName.substring(0, Math.min(14, liquidName.length())), 50, 25, 4210752);
            this.font.draw(poseStack, amount + " " + liquidStack.getAmount() + " mB", 50, 35, 4210752);
            this.font.draw(poseStack, temperature + " " + (liquidAttributes.getTemperature() - 273) + "C", 50, 45, 4210752);
            this.font.draw(poseStack, luminosity + " " + liquidAttributes.getLuminosity(), 50, 55, 4210752);
            this.font.draw(poseStack, gaseous + " " + (liquidAttributes.isGaseous() ? yes : no), 50, 65, 4210752);
        }
        else {
            this.font.draw(poseStack, liquid + " " + empty, 50, 25, 4210752);
        }

    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(poseStack, partialTicks, mouseX, mouseY);

        FluidStack fluid = ((BlockEntityDimTank) tileOwner).liquidState.clientLiquid;
        int z = getFluidScaled(60, fluid.getAmount());
        TextureAtlasSprite fluidTexture = FluidUtils.getFluidTexture(fluid);

        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

        FluidUtils.color(FluidUtils.getLiquidColorWithBiome(fluid, tileOwner));
        ScreenDimTank.blit(poseStack, this.leftPos + 11, this.topPos + 21 + z, 176, 16, 60 - z, fluidTexture);
    }

    private static int getFluidScaled(int pixels, int currentLiquidAmount) {
        int maxLiquidAmount = DimTankStorage.CAPACITY;
        int x = currentLiquidAmount * pixels / maxLiquidAmount;
        return pixels - x;
    }
}
