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
import com.google.gson.JsonObject;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Is bungee player connected")
@Description("Checks if a bungee player is connected")
@Since("1.0.0")
@Examples("broadcast \"Zorg is connected !\" if bungee player named \"Zorg\" is connected")
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
            if (player.getSingle(e) == null)
                return negate;

            final JsonObject result = ConnectionClient.get().future("expressionGetPlayerConnectionState",
                    "playerUniqueId", player.getSingle(e).getUuid());
            if (negate)
                return (result.get("error").getAsBoolean());
            return !(result.get("error").getAsBoolean());
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "conenction state of a bungee player";
    }

}
