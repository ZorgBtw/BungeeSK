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

@Name("Bungee player from name")
@Description("Get bungee player from his name")
@Examples("set {_player} to bungee player named \"Zorg\"")
@Since("1.0.0, 1.1.0: Add of 'named'")
public class ExprBungeePlayerFromName extends SimpleExpression<BungeePlayer> {

    static {
        Skript.registerExpression(ExprBungeePlayerFromName.class,
                BungeePlayer.class,
                ExpressionType.SIMPLE,
                "bungee[ ]player (with name|named) %string%");
    }

    private Expression<String> player;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.player = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected BungeePlayer[] get(Event e) {
        if (BungeeSK.isClientConnected()) {
            final JsonObject result = ConnectionClient.get().future("expressionGetBungeePlayerFromName",
                    "name", this.player.getSingle(e));

            if (result.get("error").getAsBoolean())
                return new BungeePlayer[0];

            return new BungeePlayer[]{new BungeePlayer(result.get("name").getAsString(), result.get("uniqueId").getAsString())};
        }
        return new BungeePlayer[0];
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
        return "bungee player named " + this.player.toString(e, debug);
    }

}
