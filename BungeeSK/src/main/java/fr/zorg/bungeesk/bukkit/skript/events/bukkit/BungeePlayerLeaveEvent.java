package fr.zorg.bungeesk.bukkit.skript.events.bukkit;

import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BungeePlayerLeaveEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private final BungeePlayer bungeePlayer;
    private final BungeeServer disconnectedFrom;

    public BungeePlayerLeaveEvent(BungeePlayer player, BungeeServer disconnectedFrom) {
        this.bungeePlayer = player;
        this.disconnectedFrom = disconnectedFrom;
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

    public BungeePlayer getBungeePlayer() {
        return this.bungeePlayer;
    }

    public BungeeServer getDisconnectedFrom() {
        return this.disconnectedFrom;
    }

}