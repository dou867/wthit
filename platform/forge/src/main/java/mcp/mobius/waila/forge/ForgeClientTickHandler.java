package mcp.mobius.waila.forge;

import mcp.mobius.waila.Waila;
import mcp.mobius.waila.api.event.WailaTooltipEvent;
import mcp.mobius.waila.hud.ClientTickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;

public class ForgeClientTickHandler extends ClientTickHandler {

    public static void registerListener() {
        MinecraftForge.EVENT_BUS.addListener((WailaTooltipEvent event) -> {
            if (!Waila.config.get().getGeneral().isDisplayTooltip())
                return;

            if (getNarrator().active() || !Waila.config.get().getGeneral().isEnableTextToSpeech())
                return;

            if (event.getCurrentTip().isEmpty())
                return;

            if (Minecraft.getInstance().screen != null && !(Minecraft.getInstance().screen instanceof ChatScreen))
                return;

            if (event.getAccessor().getBlock() == Blocks.AIR && event.getAccessor().getEntity() == null)
                return;

            String narrate = event.getCurrentTip().get(0).getString();
            if (lastNarration.equalsIgnoreCase(narrate))
                return;

            getNarrator().clear();
            getNarrator().say(narrate, true);
            lastNarration = narrate;
        });
    }

}
