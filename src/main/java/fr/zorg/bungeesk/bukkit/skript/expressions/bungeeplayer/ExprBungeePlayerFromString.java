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

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Name("Bungee player from string")
@Description("Get bungee player from his name")
@Examples("set {_player} to bungee player \"Zorg\"")
@Since("1.0.0")
public class ExprBungeePlayerFromString extends SimpleExpression<BungeePlayer> {

    static {
        Skript.registerExpression(ExprBungeePlayerFromString.class,
                BungeePlayer.class,
                ExpressionType.SIMPLE,
                "bungee[ ]player %string%");
    }

    private Expression<String> player;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected BungeePlayer[] get(Event e) {
        assert ConnectionClient.get() != null;
        String result = ConnectionClient.get().future("GETPLAYERµ" + player.getSingle(e));
        if (result.split("\\$")[1].equals("NONE")) return null;
        return new BungeePlayer[] { new BungeePlayer(result.split("\\$")[0], result.split("\\$")[1]) };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends BungeePlayer> getReturnType() {
        return BungeePlayer.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "bungee player";
    }

}
