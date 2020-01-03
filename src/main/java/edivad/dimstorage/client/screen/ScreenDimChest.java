package edivad.dimstorage.client.screen;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.GlStateManager;

import edivad.dimstorage.Main;
import edivad.dimstorage.container.ContainerDimChest;
import edivad.dimstorage.network.PacketHandler;
import edivad.dimstorage.network.packet.UpdateBlock;
import edivad.dimstorage.storage.DimChestStorage;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tools.Translate;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class ScreenDimChest extends ContainerScreen<ContainerDimChest> {

	private static final ResourceLocation background = new ResourceLocation(Main.MODID, "textures/gui/dimchest.png");

	private static final int ANIMATION_SPEED = 10;
	private static final int SETTINGS_WIDTH = 80;
	private static final int BUTTON_WIDTH = 20;

	private static enum SettingsState {
		STATE_CLOSED, STATE_OPENNING, STATE_OPENED, STATE_CLOSING
	}

	private String change, owner, freq, locked, yes, no, inventory, name;
	
	private Button ownerButton, freqButton, lockedButton;
	private TextFieldWidget freqTextField;

	private int currentFreq;

	private SettingsState state;
	private int animationState;
	private boolean drawSettings;
	private boolean settingsButtonOver;

	private boolean noConfig;

	private TileEntityDimChest ownerTile;

	public ScreenDimChest(ContainerDimChest container, PlayerInventory invPlayer, DimChestStorage chestInv, TileEntityDimChest owner, boolean drawSettings)
	{
		super(container, invPlayer, new StringTextComponent("Dimensional Chest"));
		this.ownerTile = owner;

		this.xSize = 176;//176
		this.ySize = 230;//230

		this.state = SettingsState.STATE_CLOSED;
		this.animationState = 0;
		this.drawSettings = drawSettings;
		this.settingsButtonOver = false;
		this.noConfig = false;

		if(this.drawSettings)
		{
			animationState = SETTINGS_WIDTH;
			state = SettingsState.STATE_OPENED;
		}
	}
	
	@Override
	protected void init()
	{
		super.init();
		
		// Get translation
		change = Translate.translateToLocal("gui." + Main.MODID + ".change");
		owner = Translate.translateToLocal("gui." + Main.MODID + ".owner");
		freq = Translate.translateToLocal("gui." + Main.MODID + ".frequency");
		locked = Translate.translateToLocal("gui." + Main.MODID + ".locked");
		yes = Translate.translateToLocal("gui." + Main.MODID + ".yes");
		no = Translate.translateToLocal("gui." + Main.MODID + ".no");
		inventory = Translate.translateToLocal("container.inventory");
		name = Translate.translateToLocal("tile." + Main.MODID + ".dimensional_chest.name");

		// init buttons list
		this.buttons.clear();

		ownerButton = new Button(this.width / 2 + 95, this.height / 2 - 42, 64, 20, change, button -> change("owner"));
		freqButton = new Button(this.width / 2 + 95, this.height / 2 + 19, 64, 20, change, button -> change("freq"));
		lockedButton = new Button(this.width / 2 + 95, this.height / 2 + 58, 64, 20, no, button -> change("lock"));
		this.addButton(ownerButton);
		this.addButton(freqButton);
		//fix this
		this.addButton(lockedButton);

		// add Freq textfield
		currentFreq = ownerTile.frequency.getChannel();
		freqTextField = new TextFieldWidget(this.font, this.width / 2 + 95, this.height / 2, 64, 15, String.valueOf(currentFreq));
		freqTextField.setMaxStringLength(3);
		freqTextField.setFocused2(false);

		drawSettings(drawSettings);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		
		//freqTextField.updateCursorCounter();

		if(state == SettingsState.STATE_OPENNING)
		{
			animationState += ANIMATION_SPEED;
			if(animationState >= SETTINGS_WIDTH)
			{
				animationState = SETTINGS_WIDTH;
				state = SettingsState.STATE_OPENED;
				drawSettings(true);
			}
		}
		else if(state == SettingsState.STATE_CLOSING)
		{
			animationState -= ANIMATION_SPEED;
			if(animationState <= 0)
			{
				animationState = 0;
				state = SettingsState.STATE_CLOSED;
			}
		}
		super.render(mouseX, mouseY, partialTicks);
	}
	
	private void change(String action)
	{
		if(action == "owner")
		{
			ownerTile.swapOwner();
		}
		else if(action == "freq")
		{
			try
			{
				int freq = Math.abs(Integer.parseInt(freqTextField.getText()));
				ownerTile.setFreq(ownerTile.frequency.copy().setChannel(freq));
				currentFreq = freq;
			}
			catch(Exception e)
			{
				freqTextField.setText(String.valueOf(currentFreq));
			}
		}
		else if(action == "lock")
		{
			ownerTile.swapLocked();
		}
		PacketHandler.INSTANCE.sendToServer(new UpdateBlock(ownerTile));
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if((keyCode >= GLFW.GLFW_KEY_0 && keyCode <= GLFW.GLFW_KEY_9) || keyCode == GLFW.GLFW_KEY_DELETE)
		{
			freqTextField.keyPressed(keyCode, scanCode, modifiers);
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int clickedButton)
	{
		boolean clicked = super.mouseClicked(mouseX, mouseY, clickedButton);
		if(noConfig)
			return clicked;

		freqTextField.mouseClicked(mouseX, mouseY, clickedButton);

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		int buttonX = x + this.xSize;
		int buttonY = y + 16;

		boolean over = false;

		if(mouseX >= buttonX && mouseX <= buttonX + BUTTON_WIDTH)
			if(mouseY >= buttonY && mouseY <= buttonY + BUTTON_WIDTH)
				over = true;

		if(!over)
			return clicked;

		if(state == SettingsState.STATE_CLOSED)
		{
			state = SettingsState.STATE_OPENNING;
		}
		else if(state == SettingsState.STATE_OPENED)
		{
			state = SettingsState.STATE_CLOSING;
			drawSettings(false);
		}
		
		return clicked;
	}

//	@Override
//	public void handleMouseInput() throws IOException
//	{
//		super.handleMouseInput();
//
//		int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
//		int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
//
//		int x = (this.width - this.xSize) / 2;
//		int y = (this.height - this.ySize) / 2;
//
//		int buttonX = x + this.xSize;
//		int buttonY = y + 16;
//
//		this.settingsButtonOver = false;
//
//		if(mouseX >= buttonX && mouseX <= buttonX + BUTTON_WIDTH)
//			if(mouseY >= buttonY && mouseY <= buttonY + BUTTON_WIDTH)
//				settingsButtonOver = true;
//	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(background);

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		int settingsX = x + (this.xSize - SETTINGS_WIDTH);

		if(!noConfig)
			this.blit(settingsX + this.animationState, y + 36, this.xSize, 36, SETTINGS_WIDTH, this.ySize);

		this.blit(x, y, 0, 0, this.xSize, 222);

		int buttonX = x + this.xSize;
		int buttonY = y + 16;

		// button background
		this.blit(buttonX, buttonY, this.xSize, 16, BUTTON_WIDTH, BUTTON_WIDTH);

		if(state == SettingsState.STATE_CLOSED || state == SettingsState.STATE_OPENNING)
		{
			if(settingsButtonOver)
				this.blit(buttonX + 6, buttonY - 3, this.xSize + 28, 16, 8, BUTTON_WIDTH);
			else
				this.blit(buttonX + 6, buttonY - 3, this.xSize + 20, 16, 8, BUTTON_WIDTH);
		}
		else if(state == SettingsState.STATE_OPENED || state == SettingsState.STATE_CLOSING)
		{
			if(settingsButtonOver)
				this.blit(buttonX + 4, buttonY - 3, this.xSize + 44, 16, 8, BUTTON_WIDTH);
			else
				this.blit(buttonX + 4, buttonY - 3, this.xSize + 36, 16, 8, BUTTON_WIDTH);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.font.drawString(name, 8, 6, 4210752);
		this.font.drawString(inventory, 8, 128, 4210752);

		if(!drawSettings)
			return;

		int posY = 45;

		// owner
		this.font.drawString(owner, 185, posY, 4210752);
		posY += 9;
		this.hLine(185, 185 + this.font.getStringWidth(owner), posY, 0xFF333333);
		posY += 6;
		int width = this.font.getStringWidth(ownerTile.frequency.getOwner());
		this.font.drawString(ownerTile.frequency.getOwner(), 215 - width / 2, posY, 4210752);
		posY += 40;

		// freq
		this.font.drawString(freq.toString(), 185, posY, 4210752);
		posY += 9;
		this.hLine(185, 185 + this.font.getStringWidth(freq), posY, 0xFF333333);
		posY += 51;

		// locked
		this.font.drawString(locked, 185, posY, 4210752);
		posY += 9;
		this.hLine(185, 185 + this.font.getStringWidth(locked), posY, 0xFF333333);

		// refresh button label
		this.lockedButton.setMessage(ownerTile.locked ? this.yes : this.no);
	}
	
	

//	@Override
//	public void drawScreen(int par1, int par2, float par3)
//	{
//		super.drawScreen(par1, par2, par3);
//
//		// freq
//		freqTextField.drawTextBox();
//	}

	private void drawSettings(boolean draw)
	{
		drawSettings = draw;

		ownerButton.visible = draw;
		freqButton.visible = draw;
		lockedButton.visible = draw;

		freqTextField.setVisible(draw);
	}
}
