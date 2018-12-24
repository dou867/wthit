package mcp.mobius.waila.gui.config.value;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontRenderer;
import net.minecraft.client.gui.GuiEventListener;
import net.minecraft.client.gui.widget.TextFieldWidget;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class OptionsEntryValueInput<T> extends OptionsEntryValue<T> {

    public static final Predicate<String> ANY = s -> true;
    public static final Predicate<String> INTEGER = s -> s.matches("^[0-9]*$");
    public static final Predicate<String> FLOAT = s -> s.matches("[-+]?([0-9]*\\.[0-9]+|[0-9]+)") || s.endsWith(".") || s.isEmpty();
    private static final MethodHandle _SET_Y;
    static {
        try {
            Field _x = TextFieldWidget.class.getDeclaredFields()[3];
            _x.setAccessible(true);
            _SET_Y = MethodHandles.lookup().unreflectSetter(_x);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final TextFieldWidget textField;

    public OptionsEntryValueInput(String optionName, T value, Consumer<T> save, Predicate<String> validator) {
        super(optionName, save);

        this.value = value;
        this.textField = new WatchedTextfield(this, 0, MinecraftClient.getInstance().fontRenderer, 0, 0, 98, 18);
        textField.setText(String.valueOf(value));
        textField.method_1890(validator);
    }

    public OptionsEntryValueInput(String optionName, T value, Consumer<T> save) {
        this(optionName, value, save, s -> true);
    }

    @Override
    protected void drawValue(int entryWidth, int entryHeight, int x, int y, int mouseX, int mouseY, boolean selected, float partialTicks) {
        textField.setX(x + 135);
        try {
            _SET_Y.bindTo(textField).invoke(y + entryHeight / 6);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        textField.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public GuiEventListener getListener() {
        return textField;
    }

    @SuppressWarnings("unchecked")
    private void setValue(String text) {
        if (value instanceof String)
            value = (T) text;

        try {
            if (value instanceof Integer)
                value = (T) Integer.valueOf(text);
            else if (value instanceof Short)
                value = (T) Short.valueOf(text);
            else if (value instanceof Byte)
                value = (T) Byte.valueOf(text);
            else if (value instanceof Long)
                value = (T) Long.valueOf(text);
            else if (value instanceof Double)
                value = (T) Double.valueOf(text);
            else if (value instanceof Float)
                value = (T) Float.valueOf(text);
        } catch (NumberFormatException e) {
            // no-op
        }
    }

    private static class WatchedTextfield extends TextFieldWidget {
        private final OptionsEntryValueInput<?> watcher;

        public WatchedTextfield(OptionsEntryValueInput<?> watcher, int id, FontRenderer fontRenderer, int x, int y, int width, int height) {
            super(id, fontRenderer, x, y, width, height);

            this.watcher = watcher;
        }

        @Override
        public void addText(String string) {
            super.addText(string);
            watcher.setValue(getText());
        }

        @Override
        public void setText(String value) {
            super.setText(value);
            watcher.setValue(getText());
        }

        public void method_1878(int count) {
            super.method_1878(count);
            watcher.setValue(getText());
        }
    }
}