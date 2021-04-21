package fr.zorg.bungeesk.bungee.listeners;

import fr.zorg.bungeesk.bungee.BungeeSK;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServSwitchEvent implements Listener {

    @EventHandler
    public void onSwitch(ServerSwitchEvent e) {
        final StringBuilder builder = new StringBuilder().append("SERVERSWITCHEVENTµ");
        if (e.getFrom() == null) return;
        builder.append(e.getPlayer().toString()).append("$");
        builder.append(e.getPlayer().getUniqueId().toString()).append("^");
        builder.append(e.getFrom().getName());
        BungeeSK.getInstance().getServer().writeAll(builder.toString());
    }

}
