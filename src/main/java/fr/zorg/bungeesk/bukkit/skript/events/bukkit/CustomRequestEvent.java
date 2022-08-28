package fr.zorg.bungeesk.bukkit.skript.events.bukkit;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomRequestEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private final String name;
    private Object response;

    public CustomRequestEvent(String name) {
        super(true);
        this.name = name;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public String getName() {
        return this.name;
    }

    public Object getResponse() {
        return this.response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

}