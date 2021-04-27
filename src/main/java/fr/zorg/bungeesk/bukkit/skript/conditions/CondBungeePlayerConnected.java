package fr.zorg.bungeesk.bukkit.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Name("Is bungee player connected")
@Description("Checks if a bungee player is connected")
@Since("1.0.0")
@Examples("broadcast \"God is connected !\" if bungeeplayer \"Zorg\" is connected")
public class CondBungeePlayerConnected extends Condition {

    static {
        Skript.registerCondition(CondBungeePlayerConnected.class,
                "%bungeeplayer% is connected",
                "%bungeeplayer% is(n't| not) connected");
    }

    private Expression<BungeePlayer> player;
    private boolean negate;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        negate = (matchedPattern == 1);
        player = (Expression<BungeePlayer>) exprs[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (BungeeSK.isClientConnected()) {
            String result = ConnectionClient.get().future("ISCONNECTEDµ" + player.getSingle(e).getData());
            if (negate) {
                return (result.equals("FALSE"));
            }
            return (result.equals("TRUE"));
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "conenction state of a bungee player";
    }

}
