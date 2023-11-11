package fr.zorg.bungeesk.bukkit.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.ClientDisconnectEvent;

public class EvtClientDisconnect {

    static {
        Skript.registerEvent("client disconnect", SimpleEvent.class, ClientDisconnectEvent.class,
                        "[bungee] client disconnect")
                .description("When the client disconnects from the server")
                .examples("on bungee client disconnect:", "\tbroadcast \"&cClient disconnected !\"")
                .since("1.0.0");
    }

}