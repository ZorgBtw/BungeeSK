package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeplayer;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Name("All of the bungee players")
@Description("Returns every bungee player on the network")
@Examples("loop all bungee players:\n" +
        "\tsend \"%loop-value%\" is connected !")
@Since("1.0.0")
public class ExprBungeePlayers extends SimpleExpression<BungeePlayer> {

    static {
        Skript.registerExpression(ExprBungeePlayers.class, BungeePlayer.class, ExpressionType.SIMPLE,
                "[(all [[of] the]|the)] bungee players");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected @Nullable BungeePlayer[] get(Event e) {
        assert ConnectionClient.get() != null;
        CompletableFuture<String> future = new CompletableFuture<>();
        new Thread(() -> {
            LinkedList<CompletableFuture<String>> completableFutures = ConnectionClient.get().getToComplete().get("ALLBUNGEEPLAYERS");
            if (completableFutures == null) completableFutures = new LinkedList<>();
            completableFutures.add(future);
            ConnectionClient.get().getToComplete().put("ALLBUNGEEPLAYERS", completableFutures);
            ConnectionClient.get().write("EXPRALLBUNGEEPLAYERS");
        }).start();
        String result;
        try {
            result = future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException interruptedException) {
            return null;
        }
        List<BungeePlayer> players = new ArrayList<>();
        if (result.equals("NONE^")) return null;
        for (String player : result.split("\\^")) {
            String name = player.split("\\$")[0];
            String uuid = player.split("\\$")[1];
            players.add(new BungeePlayer(name, uuid));
        }
        return players.toArray(new BungeePlayer[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends BungeePlayer> getReturnType() {
        return BungeePlayer.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "bungee players";
    }

}
