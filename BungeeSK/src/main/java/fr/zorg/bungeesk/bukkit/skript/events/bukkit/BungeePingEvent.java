package fr.zorg.bungeesk.bukkit.skript.events.bukkit;

import fr.zorg.bungeesk.common.entities.Ping;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.net.InetAddress;

public class BungeePingEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private final InetAddress address;
    private final Ping ping;

    public BungeePingEvent(InetAddress address, Ping ping) {
        super(true);
        this.address = address;
        this.ping = ping;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Ping getPing() {
        return this.ping;
    }

    public InetAddress getAddress() {
        return this.address;
    }
}