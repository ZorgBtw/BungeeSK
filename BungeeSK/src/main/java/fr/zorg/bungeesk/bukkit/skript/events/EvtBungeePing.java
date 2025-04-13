package fr.zorg.bungeesk.bukkit.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.BungeePingEvent;

public class EvtBungeePing {

    static {
        Skript.registerEvent("proxy ping", SimpleEvent.class, BungeePingEvent.class,
                        "(proxy|bungee) ping")
                .description("When the proxy is being ping by a player. Need to inform the proxy " +
                        "that this server is listening to this event with the 'Listen to proxy ping' effect")
                .examples("on proxy ping:", "\tset max players to 10", "\tset connected players to 5", "\t#And so on...")
                .since("2.0.0");
    }

}