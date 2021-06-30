package fr.zorg.bungeesk.bukkit.skript.events.bukkit;

import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import fr.zorg.bungeesk.bukkit.utils.BungeeServer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerSwitchEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private final BungeePlayer player;
    private final BungeeServer server;

    public ServerSwitchEvent(BungeePlayer player, BungeeServer server) {
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

    public BungeeServer getServer() {
        return this.server;
    }
}
