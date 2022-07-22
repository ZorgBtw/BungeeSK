package fr.zorg.bungeesk.bungee.utils;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeUtils {

    public static ProxiedPlayer getPlayer(BungeePlayer bungeePlayer) {
        return BungeeSK.getInstance().getProxy().getPlayer(bungeePlayer.getUuid());
    }

    public static TextComponent[] getTextComponent(String... text) {
        final TextComponent[] textComponents = new TextComponent[text.length];
        for (int i = 0; i < text.length; i++) {
            textComponents[i] = new TextComponent(text[i]);
        }
        return textComponents;
    }

}