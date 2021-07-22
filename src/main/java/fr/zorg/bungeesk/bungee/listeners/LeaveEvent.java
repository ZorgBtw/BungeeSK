package fr.zorg.bungeesk.bungee.listeners;

import fr.zorg.bungeesk.bungee.BungeeSK;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LeaveEvent implements Listener {

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e) {
        BungeeSK.getInstance().getServer().writeAll(true, "eventBungeePlayerDisconnect",
                "name", e.getPlayer().getName(),
                "uniqueId", e.getPlayer().getUniqueId().toString());
    }

}
