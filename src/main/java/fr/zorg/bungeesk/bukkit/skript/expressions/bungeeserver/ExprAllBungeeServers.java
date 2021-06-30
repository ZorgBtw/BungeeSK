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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import fr.zorg.bungeesk.bukkit.utils.BungeeServer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("All of the bungee servers")
@Description("Returns every server in the bungeecord config")
@Examples("loop all bungee servers:\n" +
        "\tsend \"%loop-value%\"")
@Since("1.0.0, 1.1.0: Returns BungeeServer")
public class ExprAllBungeeServers extends SimpleExpression<BungeeServer> {

    static {
        Skript.registerExpression(ExprAllBungeeServers.class, BungeeServer.class,
                ExpressionType.SIMPLE, "[(all [[of] the]|the)] [bungee] servers");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected @Nullable BungeeServer[] get(Event e) {
        if (BungeeSK.isClientConnected()) {
            final JsonArray result = ConnectionClient.get().future("expressionGetAllBungeeServers").get("servers").getAsJsonArray();
            BungeeServer[] servers = new BungeeServer[result.size()];
            for (int i = 0; i < result.size(); i++) {
                final JsonObject serverData = result.get(i).getAsJsonObject();
                servers[i] = new BungeeServer(serverData.get("address").getAsString(),
                        serverData.get("port").getAsInt(),
                        serverData.get("name").getAsString(),
                        serverData.get("motd").getAsString());
            }
            return servers;
        }
        return new BungeeServer[0];
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends BungeeServer> getReturnType() {
        return BungeeServer.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "all bungee servers";
    }

}
