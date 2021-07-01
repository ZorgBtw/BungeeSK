package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeserver;

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
import fr.zorg.bungeesk.bukkit.utils.BungeeServer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Bungee server from its name")
@Description("Get bungee server from its name")
@Examples("set {_player} to bungee server named \"Hub\"")
@Since("1.1.0")
public class ExprBungeeServerFromName extends SimpleExpression<BungeeServer> {

    static {
        Skript.registerExpression(ExprBungeeServerFromName.class,
                BungeeServer.class,
                ExpressionType.SIMPLE,
                "bungee[ ]server (with name|named) %string%");
    }

    private Expression<String> name;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.name = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected BungeeServer[] get(Event e) {
        if (BungeeSK.isClientConnected()) {
            final JsonObject result = ConnectionClient.get().future("expressionGetBungeeServerFromName", "name", this.name.getSingle(e));

            if (result.get("error").getAsBoolean()) {
                return new BungeeServer[0];
            }
            return new BungeeServer[]{new BungeeServer(result.get("address").getAsString(),
                    result.get("port").getAsInt(),
                    result.get("name").getAsString(),
                    result.get("motd").getAsString())};
        }
        return new BungeeServer[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends BungeeServer> getReturnType() {
        return BungeeServer.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "bungee server with name " + this.name.toString(e, debug);
    }

}
