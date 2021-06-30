package fr.zorg.bungeesk.bungee.listeners;

import fr.zorg.bungeesk.bungee.BungeeSK;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginEvent implements Listener {

    @EventHandler
    public void onLogin(PostLoginEvent e) {
        BungeeSK.getInstance().getServer().writeAll(true, "eventBungeePlayerConnect",
                "name", e.getPlayer().getName(),
                "uniqueId", e.getPlayer().getUniqueId().toString());
    }

}
