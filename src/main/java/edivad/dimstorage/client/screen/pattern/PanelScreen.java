package edivad.dimstorage.client.screen.pattern;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import edivad.dimstorage.tools.Config;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class PanelScreen<T extends AbstractContainerMenu> extends BaseScreen<T> {

    private static final int ANIMATION_SPEED = 10;
    private static final int SETTINGS_WIDTH = 80;
    private static final int SETTINGS_HEIGHT = 180;
    private static final int BUTTON_WIDTH = 20;

    private static enum SettingsState {
        STATE_CLOSED, STATE_OPENING, STATE_OPENED, STATE_CLOSING
    }

    private SettingsState state;
    private int animationState;
    protected boolean drawSettings;
    private boolean settingsButtonOver;
    private boolean allowConfig;

    private List<AbstractWidget> component;

    public PanelScreen(T container, Inventory invPlayer, Component text, ResourceLocation background, boolean drawSettings)
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
    public void render(PoseStack mStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(mStack, mouseX, mouseY, partialTicks);

        if(state == SettingsState.STATE_OPENING)
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
        return leftPos + imageWidth;
    }

    private int getButtonY()
    {
        return topPos + 16;
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
                        state = SettingsState.STATE_OPENING;
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
    protected void renderBg(PoseStack mStack, float partialTicks, int mouseX, int mouseY)
    {
        super.renderBg(mStack, partialTicks, mouseX, mouseY);
        int settingsX = leftPos + (this.imageWidth - SETTINGS_WIDTH);

        if(allowConfig)
            this.blit(mStack, settingsX + this.animationState, topPos + 36, this.imageWidth, 36, SETTINGS_WIDTH, this.imageHeight);

        this.blit(mStack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight + 2);//Space to see the border

        // button background
        this.blit(mStack, getButtonX(), getButtonY(), this.imageWidth, 16, BUTTON_WIDTH, BUTTON_WIDTH);

        if(state == SettingsState.STATE_CLOSED || state == SettingsState.STATE_OPENING)
        {
            if(settingsButtonOver)
                this.blit(mStack, getButtonX() + 6, getButtonY() - 3, this.imageWidth + 28, 16, 8, BUTTON_WIDTH);
            else
                this.blit(mStack, getButtonX() + 6, getButtonY() - 3, this.imageWidth + 20, 16, 8, BUTTON_WIDTH);
        }
        else if(state == SettingsState.STATE_OPENED || state == SettingsState.STATE_CLOSING)
        {
            if(settingsButtonOver)
                this.blit(mStack, getButtonX() + 4, getButtonY() - 3, this.imageWidth + 44, 16, 8, BUTTON_WIDTH);
            else
                this.blit(mStack, getButtonX() + 4, getButtonY() - 3, this.imageWidth + 36, 16, 8, BUTTON_WIDTH);
        }
    }

    public List<Rect2i> getAreas()
    {
        List<Rect2i> extraAreas = new ArrayList<>();
        extraAreas.add(new Rect2i(leftPos + imageWidth, getButtonY(), BUTTON_WIDTH, BUTTON_WIDTH));
        extraAreas.add(new Rect2i(leftPos + imageWidth, getButtonY() + BUTTON_WIDTH, animationState, SETTINGS_HEIGHT));
        return extraAreas;
    }

    protected void clearComponent()
    {
        /*for(AbstractWidget widget : component)
        {
            if(widget instanceof Button)
            {
                this.buttons.clear();
                break;
            }
        }*/
        component.clear();
    }

    protected void addComponent(AbstractWidget widget)
    {
        component.add(widget);
        addRenderableWidget(widget);
        /*if(widget instanceof Button button)
            addButton(button);
        else if(widget instanceof EditBox editBox)
            children.add(editBox);*/
    }

    protected void drawSettings(boolean draw)
    {
        drawSettings = draw;
        for(AbstractWidget widget : component)
            widget.visible = draw;
    }
}
