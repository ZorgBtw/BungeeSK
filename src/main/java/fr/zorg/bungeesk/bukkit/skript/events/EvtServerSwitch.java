package fr.zorg.bungeesk.bukkit.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.ServerSwitchEvent;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import org.jetbrains.annotations.Nullable;

public class EvtServerSwitch {

    static {
        Skript.registerEvent("server switch", SimpleEvent.class, ServerSwitchEvent.class,
                "[bungee] server switch")
                .description("When the player switches between 2 servers")
                .examples("on server switch:", "\tbroadcast \"&6%event-bungeeplayer% &7switched from server &c%past-server% &7to &a%event-bungeeplayer's server% &7!\"")
                .since("1.0.0");


        EventValues.registerEventValue(ServerSwitchEvent.class, BungeePlayer.class, new Getter<BungeePlayer, ServerSwitchEvent>() {
            @Nullable
            @Override
            public BungeePlayer get(ServerSwitchEvent e) {
                return e.getPlayer();
            }
        }, 0);

        EventValues.registerEventValue(ServerSwitchEvent.class, String.class, new Getter<String, ServerSwitchEvent>() {
            @Nullable
            @Override
            public String get(ServerSwitchEvent e) {
                return e.getServer();
            }
        }, 0);
    }

}
