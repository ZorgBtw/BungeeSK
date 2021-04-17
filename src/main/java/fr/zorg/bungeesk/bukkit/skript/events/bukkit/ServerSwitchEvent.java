package fr.zorg.bungeesk.bukkit.skript.events.bukkit;

import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerSwitchEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private final BungeePlayer player;
    private final String server;

    public ServerSwitchEvent(BungeePlayer player, String server) {
        this.player = player;
        this.server = server;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public BungeePlayer getPlayer() {
        return this.player;
    }

    public String getServer() {
        return this.server;
    }
}
