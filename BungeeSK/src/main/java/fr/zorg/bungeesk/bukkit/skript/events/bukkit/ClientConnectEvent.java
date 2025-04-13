package fr.zorg.bungeesk.bukkit.skript.events.bukkit;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClientConnectEvent extends Event {

    public static final HandlerList handlers = new HandlerList();

    public ClientConnectEvent() {
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}