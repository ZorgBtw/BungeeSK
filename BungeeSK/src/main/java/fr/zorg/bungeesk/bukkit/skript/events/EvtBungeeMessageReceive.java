package fr.zorg.bungeesk.bukkit.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.BungeeMessageReceiveEvent;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import org.jetbrains.annotations.Nullable;

public class EvtBungeeMessageReceive {

    static {
        Skript.registerEvent("bungee message receive", SimpleEvent.class, BungeeMessageReceiveEvent.class,
                        "bungee [custom] message receive")
                .description("When a bungee message is received")
                .examples("on bungee message receive:", "\tset {_server} to event-bungeeserver", "\tset {_message} to event-string")
                .since("1.1.0");

        EventValues.registerEventValue(BungeeMessageReceiveEvent.class, BungeeServer.class, new Getter<BungeeServer, BungeeMessageReceiveEvent>() {
            @Nullable
            @Override
            public BungeeServer get(BungeeMessageReceiveEvent e) {
                return e.getFrom();
            }
        }, 0);

        EventValues.registerEventValue(BungeeMessageReceiveEvent.class, String.class, new Getter<String, BungeeMessageReceiveEvent>() {
            @Nullable
            @Override
            public String get(BungeeMessageReceiveEvent e) {
                return e.getMessage();
            }
        }, 0);
    }

}