package edivad.dimstorage.client.gui;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import edivad.dimstorage.Main;
import edivad.dimstorage.container.ContainerDimChest;
import edivad.dimstorage.network.test.DoBlockUpdate;
import edivad.dimstorage.network.test.PacketHandler;
import edivad.dimstorage.storage.DimChestStorage;
import edivad.dimstorage.tile.TileEntityDimChest;
import edivad.dimstorage.tools.Translate;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiDimChest extends GuiContainer {

	private static final ResourceLocation background = new ResourceLocation(Main.MODID, "gui/dimchest.png");

	private static final int BUTTON_OWNER = 1;
	private static final int BUTTON_FREQ = 2;
	private static final int BUTTON_LOCKED = 3;

	private static final int ANIMATION_SPEED = 10;
	private static final int SETTINGS_WIDTH = 80;
	private static final int BUTTON_WIDTH = 20;

	private static enum SettingsState {
		STATE_CLOSED, STATE_OPENNING, STATE_OPENED, STATE_CLOSING
	}

	private String change, owner, freq, locked, yes, no, inventory, name;

	private GuiButton ownerButton, freqButton, lockedButton;
	private GuiTextField freqTextField;

	private int currentFreq;

	private SettingsState state;
	private int animationState;
	private boolean drawSettings;
	private boolean settingsButtonOver;

	private boolean noConfig;

	private TileEntityDimChest ownerTile;
	private InventoryPlayer invPlayer;

	public GuiDimChest(InventoryPlayer invPlayer, DimChestStorage chestInv, TileEntityDimChest owner)
	{
		super(new ContainerDimChest(invPlayer, chestInv));
		this.ownerTile = owner;
		this.invPlayer = invPlayer;

		this.xSize = 176;//176
		this.ySize = 230;//230

		this.state = SettingsState.STATE_CLOSED;
		this.animationState = 0;
		this.drawSettings = false;
		this.settingsButtonOver = false;
		this.noConfig = false;
	}

	@Override
	public void initGui()
	{
		super.initGui();

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
		this.buttonList.clear();

		ownerButton = new GuiButton(BUTTON_OWNER, this.width / 2 + 95, this.height / 2 - 42, 64, 20, change);
		this.buttonList.add(ownerButton);

		freqButton = new GuiButton(BUTTON_FREQ, this.width / 2 + 95, this.height / 2 + 19, 64, 20, change);
		this.buttonList.add(this.freqButton);

		lockedButton = new GuiButton(BUTTON_LOCKED, this.width / 2 + 95, this.height / 2 + 58, 64, 20, no);
		this.buttonList.add(lockedButton);

		// add Freq textfield
		currentFreq = ownerTile.frequency.getChannel();
		freqTextField = new GuiTextField(0, this.fontRenderer, this.width / 2 + 95, this.height / 2, 64, 15);
		freqTextField.setMaxStringLength(3);
		freqTextField.setFocused(false);
		freqTextField.setText(String.valueOf(currentFreq));

		drawSettings(drawSettings);
	}

	@Override
	public void updateScreen()
	{
		freqTextField.updateCursorCounter();

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
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button.id == BUTTON_OWNER)
		{
			ownerTile.swapOwner();
			PacketHandler.packetReq.sendToServer(new DoBlockUpdate(ownerTile));
			this.inventorySlots = new ContainerDimChest(invPlayer, ownerTile.getStorage());
		}
		else if(button.id == BUTTON_FREQ)
		{
			try
			{
				int freq = Integer.parseInt(freqTextField.getText());
				ownerTile.setFreq(ownerTile.frequency.copy().setChannel(freq));
				PacketHandler.packetReq.sendToServer(new DoBlockUpdate(ownerTile));
				this.inventorySlots = new ContainerDimChest(invPlayer, ownerTile.getStorage());
				currentFreq = freq;
			}
			catch(Exception e)
			{
				freqTextField.setText(String.valueOf(currentFreq));
			}
		}
		else if(button.id == BUTTON_LOCKED)
		{
			ownerTile.swapLocked();
			PacketHandler.packetReq.sendToServer(new DoBlockUpdate(ownerTile));
		}
	}

	@Override
	protected void keyTyped(char c, int code) throws IOException
	{
		super.keyTyped(c, code);
		freqTextField.textboxKeyTyped(c, code);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int par3) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, par3);

		if(noConfig)
			return;

		freqTextField.mouseClicked(mouseX, mouseY, par3);

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		int buttonX = x + this.xSize;
		int buttonY = y + 16;

		boolean over = false;

		if(mouseX >= buttonX && mouseX <= buttonX + BUTTON_WIDTH)
			if(mouseY >= buttonY && mouseY <= buttonY + BUTTON_WIDTH)
				over = true;

		if(!over)
			return;

		if(state == SettingsState.STATE_CLOSED)
		{
			state = SettingsState.STATE_OPENNING;
		}
		else if(state == SettingsState.STATE_OPENED)
		{
			state = SettingsState.STATE_CLOSING;
			drawSettings(false);
		}
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();

		int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		int buttonX = x + this.xSize;
		int buttonY = y + 16;

		this.settingsButtonOver = false;

		if(mouseX >= buttonX && mouseX <= buttonX + BUTTON_WIDTH)
			if(mouseY >= buttonY && mouseY <= buttonY + BUTTON_WIDTH)
				settingsButtonOver = true;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(background);

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		int settingsX = x + (this.xSize - SETTINGS_WIDTH);

		if(!noConfig)
			this.drawTexturedModalRect(settingsX + this.animationState, y + 36, this.xSize, 36, SETTINGS_WIDTH, this.ySize);

		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, 222);

		int buttonX = x + this.xSize;
		int buttonY = y + 16;

		// button background
		this.drawTexturedModalRect(buttonX, buttonY, this.xSize, 16, BUTTON_WIDTH, BUTTON_WIDTH);

		if(state == SettingsState.STATE_CLOSED || state == SettingsState.STATE_OPENNING)
		{
			if(settingsButtonOver)
				this.drawTexturedModalRect(buttonX + 6, buttonY - 3, this.xSize + 28, 16, 8, BUTTON_WIDTH);
			else
				this.drawTexturedModalRect(buttonX + 6, buttonY - 3, this.xSize + 20, 16, 8, BUTTON_WIDTH);
		}
		else if(state == SettingsState.STATE_OPENED || state == SettingsState.STATE_CLOSING)
		{
			if(settingsButtonOver)
				this.drawTexturedModalRect(buttonX + 4, buttonY - 3, this.xSize + 44, 16, 8, BUTTON_WIDTH);
			else
				this.drawTexturedModalRect(buttonX + 4, buttonY - 3, this.xSize + 36, 16, 8, BUTTON_WIDTH);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		this.fontRenderer.drawString(name, 8, 6, 4210752);
		this.fontRenderer.drawString(inventory, 8, 128, 4210752);

		if(!drawSettings)
			return;

		int posY = 45;

		// owner
		this.fontRenderer.drawString(owner, 185, posY, 4210752);
		posY += 9;
		this.drawHorizontalLine(185, 185 + this.fontRenderer.getStringWidth(owner), posY, 0xFF333333);
		posY += 6;
		int width = this.fontRenderer.getStringWidth(ownerTile.frequency.getOwner());
		this.fontRenderer.drawString(ownerTile.frequency.getOwner(), 215 - width / 2, posY, 4210752);
		posY += 40;

		// freq
		this.fontRenderer.drawString(freq.toString(), 185, posY, 4210752);
		posY += 9;
		this.drawHorizontalLine(185, 185 + this.fontRenderer.getStringWidth(freq), posY, 0xFF333333);
		posY += 51;

		// locked
		this.fontRenderer.drawString(locked, 185, posY, 4210752);
		posY += 9;
		this.drawHorizontalLine(185, 185 + this.fontRenderer.getStringWidth(locked), posY, 0xFF333333);

		// refresh button label
		this.lockedButton.displayString = ownerTile.locked ? this.yes : this.no;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);

		// freq
		freqTextField.drawTextBox();
	}

	private void drawSettings(boolean draw)
	{
		drawSettings = draw;

		ownerButton.visible = draw;
		freqButton.visible = draw;
		lockedButton.visible = draw;

		freqTextField.setVisible(draw);
	}
}
