package fr.zorg.bungeesk.bukkit.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.BungeeServerStartEvent;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import org.jetbrains.annotations.Nullable;

public class EvtBungeeServerStart {

    static {
        Skript.registerEvent("bungee server start", SimpleEvent.class, BungeeServerStartEvent.class,
                        "bungee server (start|connect)")
                .description("When a bungee server is started / connected to BungeeSK")
                .examples("on bungee server start:", "\tset {_server} to event-bungeeserver")
                .since("2.0.0");

        EventValues.registerEventValue(BungeeServerStartEvent.class, BungeeServer.class, new Getter<BungeeServer, BungeeServerStartEvent>() {
            @Nullable
            @Override
            public BungeeServer get(BungeeServerStartEvent e) {
                return e.getBungeeServer();
            }
        }, 0);
    }

}