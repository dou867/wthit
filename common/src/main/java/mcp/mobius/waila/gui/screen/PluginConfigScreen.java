package mcp.mobius.waila.gui.screen;

import java.util.Set;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mcp.mobius.waila.config.ConfigEntry;
import mcp.mobius.waila.config.PluginConfig;
import mcp.mobius.waila.gui.widget.ConfigListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class PluginConfigScreen extends ConfigScreen {

    private static final String NO_CATEGORY = "no_category";

    public PluginConfigScreen(Screen parent) {
        super(parent, new TranslatableComponent("gui.waila.plugin_settings"), PluginConfig.INSTANCE::save, PluginConfig.INSTANCE::reload);
    }

    @Override
    public ConfigListWidget getOptions() {
        ConfigListWidget options = new ConfigListWidget(this, minecraft, width, height, 32, height - 32, 30, PluginConfig.INSTANCE::save);
        for (String namespace : PluginConfig.INSTANCE.getNamespaces()) {
            String translationKey = "config.waila.plugin_" + namespace;
            Set<ResourceLocation> keys = PluginConfig.INSTANCE.getKeys(namespace);
            options.withButton(translationKey, 100, 20, w -> minecraft.setScreen(new ConfigScreen(PluginConfigScreen.this, new TranslatableComponent(translationKey)) {
                @Override
                public ConfigListWidget getOptions() {
                    ConfigListWidget options = new ConfigListWidget(this, minecraft, width, height, 32, height - 32, 30);
                    Object2IntMap<String> categories = new Object2IntOpenHashMap<>();
                    categories.put(NO_CATEGORY, 0);
                    for (ResourceLocation key : keys) {
                        ConfigEntry<Object> entry = PluginConfig.INSTANCE.getEntry(key);
                        if (entry.isSynced() && Minecraft.getInstance().getCurrentServer() != null) {
                            continue;
                        }
                        String path = key.getPath();
                        String category = NO_CATEGORY;
                        if (path.contains(".")) {
                            String c = path.split("[.]", 2)[0];
                            String translation = translationKey + "." + c;
                            if (I18n.exists(translation)) {
                                category = c;
                                if (!categories.containsKey(category)) {
                                    options.withCategory(translation);
                                    categories.put(category, options.children().size());
                                }
                            }
                        }
                        int index = categories.getInt(category);
                        categories.put(category, index + 1);
                        options.with(index, entry.getType().createConfigValue(translationKey + "." + path, entry.getValue(), entry::setValue));
                    }
                    return options;
                }
            }));
        }
        return options;
    }

}