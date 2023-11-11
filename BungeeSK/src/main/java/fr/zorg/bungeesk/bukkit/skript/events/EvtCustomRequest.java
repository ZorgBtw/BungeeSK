package fr.zorg.bungeesk.bukkit.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.CustomRequestEvent;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import org.jetbrains.annotations.Nullable;

public class EvtCustomRequest {

    static {
        Skript.registerEvent("bungee custom request", SimpleEvent.class, CustomRequestEvent.class,
                        "[bungee] custom request")
                .description("When a custom request is asked from another BungeeServer")
                .examples("broadcast custom request \"hello\" from bungee server named \"lobby\"",
                        "",
                        "#On the other server",
                        "on custom request:",
                        "\tif event-string is \"hello\":",
                        "\t\tset custom request response to \"hi !\"")
                .since("2.0.0");


        EventValues.registerEventValue(CustomRequestEvent.class, String.class, new Getter<String, CustomRequestEvent>() {
            @Nullable
            @Override
            public String get(CustomRequestEvent e) {
                return e.getName();
            }
        }, 0);

        EventValues.registerEventValue(CustomRequestEvent.class, BungeeServer.class, new Getter<BungeeServer, CustomRequestEvent>() {
            @Nullable
            @Override
            public BungeeServer get(CustomRequestEvent e) {
                return e.getFrom();
            }
        }, 0);
    }

}