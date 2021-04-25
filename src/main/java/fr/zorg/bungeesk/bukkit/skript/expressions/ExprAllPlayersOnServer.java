package fr.zorg.bungeesk.bukkit.skript.expressions;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Name("All of the bungee players on specific server")
@Description("Returns every bungee player on a specific server")
@Examples("loop all bungee players on server \"lobby\":\n" +
        "\tsend \"%loop-bungeeplayer%\"")
@Since("1.0.2")
public class ExprAllPlayersOnServer extends SimpleExpression<BungeePlayer> {

    static {
        Skript.registerExpression(ExprAllPlayersOnServer.class,
                BungeePlayer.class,
                ExpressionType.SIMPLE,
                "[(all [[of] the]|the)] bungee players on server %string%");
    }

    private Expression<String> server;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        server = (Expression<String>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected BungeePlayer[] get(Event e) {
        assert ConnectionClient.get() != null;
        String result = ConnectionClient.get().future("ALLBUNGEEPLAYERSONSERVERµ" + server.getSingle(e));
        result = result.replace(server.getSingle(e), "");
        List<BungeePlayer> players = new ArrayList<>();
        if (result.equals("^NONE")) return new BungeePlayer[0];
        for (String player : result.replaceFirst("\\^", "").split("\\^")) {
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
        return "every bungee player connected on server " + server.toString(e, debug);
    }

}
