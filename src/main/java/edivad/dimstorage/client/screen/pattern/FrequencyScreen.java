package edivad.dimstorage.client.screen.pattern;

import edivad.dimstorage.Main;
import edivad.dimstorage.client.screen.element.button.ChangeButton;
import edivad.dimstorage.client.screen.element.button.LockButton;
import edivad.dimstorage.client.screen.element.button.OwnerButton;
import edivad.dimstorage.client.screen.element.textfield.FrequencyText;
import edivad.dimstorage.tile.TileFrequencyOwner;
import edivad.dimstorage.tools.Translate;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class FrequencyScreen<T extends Container> extends PanelScreen<T> {

	private TileFrequencyOwner tileOwner;
	
	private String owner, freq, locked;
	private FrequencyText freqTextField;
	
	protected static enum Actions {
		OWNER, FREQ, LOCK, COLLECT
	}
	
	public FrequencyScreen(T container, TileFrequencyOwner tileOwner, PlayerInventory invPlayer, ITextComponent text, ResourceLocation background, boolean drawSettings)
	{
		super(container, invPlayer, text, background, drawSettings);
		this.tileOwner = tileOwner;
	}
	
	@Override
	protected void init()
	{
		super.init();
		
		// Get translation
		owner = Translate.translateToLocal("gui." + Main.MODID + ".owner");
		freq = Translate.translateToLocal("gui." + Main.MODID + ".frequency");
		locked = Translate.translateToLocal("gui." + Main.MODID + ".locked");
		
		
		clearComponent();
		addComponent(new OwnerButton(width / 2 + 95, height / 2 - 53, tileOwner.frequency.getOwner(), button -> actions(Actions.OWNER)));
		addComponent(new ChangeButton(width / 2 + 95, height / 2 + 7, button -> actions(Actions.FREQ)));
		addComponent(new LockButton(width / 2 + 95, height / 2 + 46, tileOwner.locked, button -> actions(Actions.LOCK)));
		
		freqTextField = new FrequencyText(width / 2 + 95, height / 2 - 12, String.valueOf(tileOwner.frequency.getChannel()));
		addComponent(freqTextField);	
		drawSettings(drawSettings);
	}
	
	protected abstract void actions(Actions action);
	
	@Override
	public void tick()
	{
		super.tick();
		freqTextField.tick();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		super.render(mouseX, mouseY, partialTicks);
		freqTextField.render(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int clickedButton)
	{
		freqTextField.mouseClicked(mouseX, mouseY, clickedButton);
		return super.mouseClicked(mouseX, mouseY, clickedButton);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		if(drawSettings)
		{
			int posY = 45;

			// owner
			this.font.drawString(owner, 185, posY, 4210752);
			posY += 9;
			this.hLine(185, 185 + this.font.getStringWidth(owner), posY, 0xFF333333);
			posY += 31;

			// freq
			this.font.drawString(freq, 185, posY, 4210752);
			posY += 9;
			this.hLine(185, 185 + this.font.getStringWidth(freq), posY, 0xFF333333);
			posY += 50;

			// locked
			this.font.drawString(locked, 185, posY, 4210752);
			posY += 9;
			this.hLine(185, 185 + this.font.getStringWidth(locked), posY, 0xFF333333);
		}
	}
	
	protected void setFrequencyText(int freq)
	{
		freqTextField.setText(String.valueOf(Math.abs(freq)));
	}
	
	protected int getFrequencyText()
	{
		return Math.abs(Integer.parseInt(freqTextField.getText()));
	}
}
