package fr.zorg.bungeesk.bungee.listeners;

import fr.zorg.bungeesk.bungee.BungeeSK;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LeaveEvent implements Listener {

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e) {
        String[] data = {e.getPlayer().getName(),
                e.getPlayer().getUniqueId().toString()};
        BungeeSK.getInstance().getServer().writeAll("LeaveEventµ" + String.join("µ", data));
    }

}
