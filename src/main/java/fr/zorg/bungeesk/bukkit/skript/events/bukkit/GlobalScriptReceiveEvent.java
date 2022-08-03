package fr.zorg.bungeesk.bukkit.skript.events.bukkit;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GlobalScriptReceiveEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private final String scriptName;

    public GlobalScriptReceiveEvent(String scriptName) {
        this.scriptName = scriptName;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public String getScriptName() {
        return this.scriptName;
    }

}