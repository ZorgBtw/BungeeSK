package fr.zorg.bungeesk.bukkit.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import fr.zorg.bungeesk.bukkit.utils.BungeeServer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Broadcast message to server")
@Description("Broadcasts a message to a server in the network")
@Examples("broadcast \"&aHello world !\" to bungee server named \"hub\"")
@Since("1.0.3 - 1.1.0: Usage of BungeeServer type")
public class EffBroadcastMessageToServer extends Effect {

    static {
        Skript.registerEffect(EffBroadcastMessageToServer.class,
                "broadcast %string% to %bungeeserver%");
    }

    private Expression<String> message;
    private Expression<BungeeServer> server;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.message = (Expression<String>) exprs[0];
        this.server = (Expression<BungeeServer>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (BungeeSK.isClientConnected()) {

            if (this.server.getSingle(e) == null)
                return;

            ConnectionClient.get().write(true, "effectBroadcastMessageToServer",
                    "server", this.server.getSingle(e).getAddress() + ":" + this.server.getSingle(e).getPort(),
                    "message", message.getSingle(e));
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "broadcasts message " + message.toString(e, debug) + " to server " + server.toString(e, debug);
    }

}
