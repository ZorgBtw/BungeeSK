package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeplayer;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Name("Server of bungee player")
@Description("Get the server of a player on the network. " +
        "NOTE: Server can be get only 2 ticks or more after the bungee playar join event")
@Examples("set {_server} to event-bungeeplayer's server\n")
@Since("1.0.0")
public class ExprBungeePlayerServer extends SimplePropertyExpression<BungeePlayer, String> {

    static {
        register(ExprBungeePlayerServer.class,
                String.class,
                "server",
                "bungeeplayer");
    }

    @Nullable
    @Override
    public String convert(BungeePlayer player) {
        assert ConnectionClient.get() != null;
        String result = ConnectionClient.get().future("PLAYERSERVERµ" + player.getData());

        if (result.equals("NONE")) return null;
        return result;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        return;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "bungee player's server";
    }
}
