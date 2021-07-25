package mcp.mobius.waila.gui.widget.value;

import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;
import mcp.mobius.waila.gui.widget.ConfigListWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public abstract class ConfigValue<T> extends ConfigListWidget.Entry {

    private final Component title;
    private final String description;
    protected final Consumer<T> save;
    protected T value;
    private int x;

    public ConfigValue(String optionName, Consumer<T> save) {
        this.title = new TranslatableComponent(optionName);
        this.description = optionName + "_desc";
        this.save = save;
    }

    @Override
    public final void render(PoseStack matrices, int index, int rowTop, int rowLeft, int width, int height, int mouseX, int mouseY, boolean hovered, float deltaTime) {
        client.font.drawShadow(matrices, title.getString(), rowLeft, rowTop + (height - client.font.lineHeight) / 2f, 16777215);
        drawValue(matrices, width, height, rowLeft, rowTop, mouseX, mouseY, hovered, deltaTime);
        this.x = rowLeft;
    }

    public void save() {
        save.accept(value);
    }

    public GuiEventListener getListener() {
        return null;
    }

    public Component getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getX() {
        return x;
    }

    protected abstract void drawValue(PoseStack matrices, int width, int height, int x, int y, int mouseX, int mouseY, boolean selected, float partialTicks);

}