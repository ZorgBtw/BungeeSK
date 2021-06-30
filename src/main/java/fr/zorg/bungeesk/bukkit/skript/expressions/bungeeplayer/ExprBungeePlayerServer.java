package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeplayer;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.google.gson.JsonObject;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import fr.zorg.bungeesk.bukkit.utils.BungeeServer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Server of bungee player")
@Description("Get the server of a player on the network. " +
        "NOTE: Server can be get only 2 ticks or more after the bungee playar join event")
@Examples("set {_server} to event-bungeeplayer's server\n")
@Since("1.0.0, 1.1.0: Returns BungeeServer")
public class ExprBungeePlayerServer extends SimplePropertyExpression<BungeePlayer, BungeeServer> {

    static {
        register(ExprBungeePlayerServer.class,
                BungeeServer.class,
                "server",
                "bungeeplayer");
    }

    @Nullable
    @Override
    public BungeeServer convert(BungeePlayer player) {
        if (BungeeSK.isClientConnected()) {
            if (player == null)
                return null;

            final JsonObject result = ConnectionClient.get().future("expressionGetServerOfBungeePlayer",
                    "uniqueId", player.getUuid());

            if (result.get("error").getAsBoolean())
                return null;

            final JsonObject jsonObject = result.get("server").getAsJsonObject();

            return new BungeeServer(
                    jsonObject.get("address").getAsString(),
                    jsonObject.get("port").getAsInt(),
                    jsonObject.get("name").getAsString(),
                    jsonObject.get("motd").getAsString());
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        return;
    }

    @Override
    public Class<? extends BungeeServer> getReturnType() {
        return BungeeServer.class;
    }

    @Override
    protected String getPropertyName() {
        return "bungee player's server";
    }
}
