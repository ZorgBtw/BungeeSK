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
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("All of the bungee players")
@Description("Returns every bungee player on the network")
@Examples("loop all bungee players:\n" +
        "\tsend \"%loop-value%\" is connected !")
@Since("1.0.0")
public class ExprAllBungeePlayers extends SimpleExpression<BungeePlayer> {

    static {
        Skript.registerExpression(ExprAllBungeePlayers.class, BungeePlayer.class, ExpressionType.SIMPLE,
                "[(all [[of] the]|the)] bungee players");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected @Nullable BungeePlayer[] get(Event e) {
        if (BungeeSK.isClientConnected()) {
            final JsonObject result = ConnectionClient.get().future("expressionGetAllBungeePlayers");

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
        return "every bungee player";
    }

}
