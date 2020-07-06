package edivad.dimstorage.client.screen.pattern;

import java.util.ArrayList;
import java.util.List;

import edivad.dimstorage.tools.Config;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PanelScreen<T extends Container> extends BaseScreen<T> {

    private static final int ANIMATION_SPEED = 10;
    private static final int SETTINGS_WIDTH = 80;
    private static final int SETTINGS_HEIGHT = 180;
    private static final int BUTTON_WIDTH = 20;

    private static enum SettingsState {
        STATE_CLOSED, STATE_OPENNING, STATE_OPENED, STATE_CLOSING
    }

    private SettingsState state;
    private int animationState;
    protected boolean drawSettings;
    private boolean settingsButtonOver;
    private boolean allowConfig;

    private List<Widget> component;

    public PanelScreen(T container, PlayerInventory invPlayer, ITextComponent text, ResourceLocation background, boolean drawSettings)
    {
        super(container, invPlayer, text, background);

        this.drawSettings = drawSettings;
        this.settingsButtonOver = false;
        this.allowConfig = Config.DIMCHEST_ALLOWCONFIG.get();
        this.component = new ArrayList<>();

        if(this.drawSettings)
        {
            animationState = SETTINGS_WIDTH;
            state = SettingsState.STATE_OPENED;
        }
        else
        {
            animationState = 0;
            state = SettingsState.STATE_CLOSED;
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        super.render(mouseX, mouseY, partialTicks);

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
            drawSettings(false);
            animationState -= ANIMATION_SPEED;
            if(animationState <= 0)
            {
                animationState = 0;
                state = SettingsState.STATE_CLOSED;
            }
        }
    }

    private int getButtonX()
    {
        return guiLeft + xSize;
    }

    private int getButtonY()
    {
        return guiTop + 16;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickedButton)
    {
        super.mouseClicked(mouseX, mouseY, clickedButton);

        if(allowConfig)
        {
            if(mouseX >= getButtonX() && mouseX <= getButtonX() + BUTTON_WIDTH)
            {
                if(mouseY >= getButtonY() && mouseY <= getButtonY() + BUTTON_WIDTH)
                {
                    if(state == SettingsState.STATE_CLOSED)
                    {
                        state = SettingsState.STATE_OPENNING;
                    }
                    else if(state == SettingsState.STATE_OPENED)
                    {
                        state = SettingsState.STATE_CLOSING;
                    }
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY)
    {
        super.mouseMoved(mouseX, mouseY);

        settingsButtonOver = false;

        if(mouseX >= getButtonX() && mouseX <= getButtonX() + BUTTON_WIDTH)
            if(mouseY >= getButtonY() && mouseY <= getButtonY() + BUTTON_WIDTH)
                settingsButtonOver = true;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        int settingsX = guiLeft + (this.xSize - SETTINGS_WIDTH);

        if(allowConfig)
            this.blit(settingsX + this.animationState, guiTop + 36, this.xSize, 36, SETTINGS_WIDTH, this.ySize);

        this.blit(guiLeft, guiTop, 0, 0, this.xSize, this.ySize + 2);//Space to see the border

        // button background
        this.blit(getButtonX(), getButtonY(), this.xSize, 16, BUTTON_WIDTH, BUTTON_WIDTH);

        if(state == SettingsState.STATE_CLOSED || state == SettingsState.STATE_OPENNING)
        {
            if(settingsButtonOver)
                this.blit(getButtonX() + 6, getButtonY() - 3, this.xSize + 28, 16, 8, BUTTON_WIDTH);
            else
                this.blit(getButtonX() + 6, getButtonY() - 3, this.xSize + 20, 16, 8, BUTTON_WIDTH);
        }
        else if(state == SettingsState.STATE_OPENED || state == SettingsState.STATE_CLOSING)
        {
            if(settingsButtonOver)
                this.blit(getButtonX() + 4, getButtonY() - 3, this.xSize + 44, 16, 8, BUTTON_WIDTH);
            else
                this.blit(getButtonX() + 4, getButtonY() - 3, this.xSize + 36, 16, 8, BUTTON_WIDTH);
        }
    }

    public List<Rectangle2d> getAreas()
    {
        List<Rectangle2d> extraAreas = new ArrayList<>();
        extraAreas.add(new Rectangle2d(guiLeft + xSize, getButtonY(), BUTTON_WIDTH, BUTTON_WIDTH));
        extraAreas.add(new Rectangle2d(guiLeft + xSize, getButtonY() + BUTTON_WIDTH, animationState, SETTINGS_HEIGHT));
        return extraAreas;
    }

    protected void clearComponent()
    {
        for(Widget widget : component)
        {
            if(widget instanceof Button)
            {
                this.buttons.clear();
                break;
            }
        }
        component.clear();
    }

    protected void addComponent(Widget widget)
    {
        component.add(widget);
        if(widget instanceof Button)
            addButton((Button) widget);
        else if(widget instanceof TextFieldWidget)
            children.add((TextFieldWidget) widget);
    }

    protected void drawSettings(boolean draw)
    {
        drawSettings = draw;
        for(Widget widget : component)
            widget.visible = draw;
    }
}
