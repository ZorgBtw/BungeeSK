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
import com.google.gson.JsonObject;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import fr.zorg.bungeesk.bukkit.utils.BungeeServer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("All of the bungee players on specific server")
@Description("Returns every bungee player on a specific server")
@Examples("loop all bungee players on bungee server named \"lobby\":\n" +
        "\tsend \"%loop-bungeeplayer%\"")
@Since("1.0.2")
public class ExprAllPlayersOnServer extends SimpleExpression<BungeePlayer> {

    static {
        Skript.registerExpression(ExprAllPlayersOnServer.class,
                BungeePlayer.class,
                ExpressionType.SIMPLE,
                "[(all [[of] the]|the)] bungee players on %bungeeserver%");
    }

    private Expression<BungeeServer> server;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.server = (Expression<BungeeServer>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected BungeePlayer[] get(Event e) {
        if (BungeeSK.isClientConnected()) {

            if (server.getSingle(e) == null)
                return new BungeePlayer[0];

            final JsonObject result = ConnectionClient.get().future("expressionGetAllPlayersOnServer",
                    "address", this.server.getSingle(e).getAddress(),
                    "port", String.valueOf(this.server.getSingle(e).getPort()));

            if (result.get("error").getAsBoolean())
                return new BungeePlayer[0];

            List<BungeePlayer> players = new ArrayList<>();
            result.get("players").getAsJsonArray().forEach(player -> {
                players.add(new BungeePlayer(player.getAsJsonObject().get("name").getAsString(), player.getAsJsonObject().get("uniqueId").getAsString()));
            });

            return players.toArray(new BungeePlayer[0]);
        }
        return new BungeePlayer[0];
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
        return "every bungee player connected on server " + this.server.toString(e, debug);
    }

}
