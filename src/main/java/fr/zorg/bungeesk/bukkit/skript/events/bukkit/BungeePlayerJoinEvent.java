package fr.zorg.bungeesk.bukkit.skript.events.bukkit;

import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BungeePlayerJoinEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private final BungeePlayer bungeePlayer;

    public BungeePlayerJoinEvent(BungeePlayer player) {
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
        return bungeePlayer;
    }

    public String getUuid() {
        return bungeePlayer.getUuid();
    }

}
