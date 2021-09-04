package fr.zorg.bungeesk.bukkit.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.BungeePlayerLeaveEvent;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import org.jetbrains.annotations.Nullable;

public class EvtBungeePlayerLeave {

    static {
        Skript.registerEvent("bungee player leave", SimpleEvent.class, BungeePlayerLeaveEvent.class,
                        "bungee [player] (leave|quit)")
                .description("When a bungee player leaves the network")
                .examples("on bungee player leave:", "\tset {_player} to event-bungeeplayer")
                .since("1.0.0");

        EventValues.registerEventValue(BungeePlayerLeaveEvent.class, BungeePlayer.class, new Getter<BungeePlayer, BungeePlayerLeaveEvent>() {
            @Nullable
            @Override
            public BungeePlayer get(BungeePlayerLeaveEvent e) {
                return e.getPlayer();
            }
        }, 0);
    }

}
