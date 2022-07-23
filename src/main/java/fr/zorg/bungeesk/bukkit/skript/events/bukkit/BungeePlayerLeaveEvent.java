package fr.zorg.bungeesk.bukkit.skript.events.bukkit;

import fr.zorg.bungeesk.common.entities.BungeePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BungeePlayerLeaveEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private final BungeePlayer bungeePlayer;

    public BungeePlayerLeaveEvent(BungeePlayer player) {
        this.bungeePlayer = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public BungeePlayer getPlayer() {
        return this.bungeePlayer;
    }

}