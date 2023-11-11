package fr.zorg.bungeesk.bukkit.skript.events.bukkit;

import fr.zorg.bungeesk.common.entities.BungeeServer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BungeeServerStartEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private final BungeeServer bungeeServer;

    public BungeeServerStartEvent(BungeeServer bungeeServer) {
        this.bungeeServer = bungeeServer;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public BungeeServer getBungeeServer() {
        return this.bungeeServer;
    }

}