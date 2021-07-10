package fr.zorg.bungeesk.bukkit.skript.events.bukkit;

import fr.zorg.bungeesk.bukkit.utils.BungeeServer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BungeeMessageReceiveEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private final BungeeServer from;
    private final String message;

    public BungeeMessageReceiveEvent(final BungeeServer from, final String message) {
        this.from = from;
        this.message = message;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public BungeeServer getFrom() {
        return this.from;
    }

    public String getMessage() {
        return this.message;
    }

}
