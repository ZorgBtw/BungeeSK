package fr.zorg.bungeesk.bukkit.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.BungeePlayerJoinEvent;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import org.jetbrains.annotations.Nullable;

public class EvtBungeePlayerJoin {

    static {
        Skript.registerEvent("bungee player join", SimpleEvent.class, BungeePlayerJoinEvent.class,
                "bungee [player] join")
                .description("When a bungee player joins the network")
                .examples("on bungee player join:", "\tset {_player} to event-bungeeplayer")
                .since("1.0.0");

        EventValues.registerEventValue(BungeePlayerJoinEvent.class, BungeePlayer.class, new Getter<BungeePlayer, BungeePlayerJoinEvent>() {
            @Nullable
            @Override
            public BungeePlayer get(BungeePlayerJoinEvent e) {
                return e.getPlayer();
            }
        }, 0);
    }

}
