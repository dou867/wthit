package mcp.mobius.waila.gui.widget.value;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class CycleValue extends ConfigValue<String> {

    private final String translationKey;
    private final Button button;
    private final boolean createLocale;

    public CycleValue(String optionName, String[] values, String selected, Consumer<String> save, boolean createLocale) {
        super(optionName, save);

        this.translationKey = optionName;
        this.createLocale = createLocale;
        List<String> vals = Arrays.asList(values);
        this.button = new Button(0, 0, 100, 20,
            createLocale
                ? new TranslatableComponent(optionName + "_" + selected.replace(" ", "_").toLowerCase(Locale.ROOT))
                : new TextComponent(selected),
            w -> value = vals.get((vals.indexOf(value) + 1) % vals.size()));
        this.value = selected;
    }

    public CycleValue(String optionName, String[] values, String selected, Consumer<String> save) {
        this(optionName, values, selected, save, false);
    }

    @Override
    protected void drawValue(PoseStack matrices, int width, int height, int x, int y, int mouseX, int mouseY, boolean selected, float partialTicks) {
        this.button.x = x + width - button.getWidth();
        this.button.y = y + (height - button.getHeight()) / 2;
        this.button.setMessage(createLocale ? new TranslatableComponent(translationKey + "_" + value.replace(" ", "_").toLowerCase(Locale.ROOT)) : new TextComponent(value));
        this.button.render(matrices, mouseX, mouseY, partialTicks);
    }

    @Override
    public GuiEventListener getListener() {
        return button;
    }

}