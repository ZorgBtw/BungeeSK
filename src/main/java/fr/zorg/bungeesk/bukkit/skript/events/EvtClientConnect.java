package fr.zorg.bungeesk.bukkit.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.ClientConnectEvent;

public class EvtClientConnect {

    static {
        Skript.registerEvent("client connect", SimpleEvent.class, ClientConnectEvent.class,
                        "[bungee] client connect")
                .description("When the client connects to a server")
                .examples("on bungee client connect:", "\tbroadcast \"&aClient connected !\"")
                .since("1.0.0");
    }
}
