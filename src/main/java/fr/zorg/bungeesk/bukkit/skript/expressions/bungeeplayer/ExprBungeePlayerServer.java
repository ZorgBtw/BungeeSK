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
        CompletableFuture<String> future = new CompletableFuture<>();
        String playerData = player.getData();
        new Thread(() -> {
            LinkedList<CompletableFuture<String>> completableFutures = ConnectionClient.get().getToComplete().get("EXPRBUNGEEPLAYERSERVERµ" + playerData);
            if (completableFutures == null) completableFutures = new LinkedList<>();
            completableFutures.add(future);
            ConnectionClient.get().getToComplete().put("EXPRBUNGEEPLAYERSERVERµ" + playerData, completableFutures);
            ConnectionClient.get().write("EXPRBUNGEEPLAYERSERVERµ" + playerData);
        }).start();
        String result;
        try {
            result = future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException interruptedException) {
            return null;
        }
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
