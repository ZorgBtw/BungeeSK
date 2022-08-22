package fr.zorg.bungeesk.bukkit.skript.events.bukkit;

import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BungeeCommandEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private final BungeePlayer player;
    private final String command;

    public BungeeCommandEvent(BungeePlayer player, String command) {
        this.player = player;
        this.command = command;
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

    public String getCommand() {
        return this.command;
    }
}