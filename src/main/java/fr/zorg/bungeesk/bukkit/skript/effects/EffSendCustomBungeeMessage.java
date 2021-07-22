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

@Name("Send custom bungee message to a server")
@Description("Sends a custom bungee message in string form to one or more servers")
@Examples("send custom message \"This is an example\" to bungee server named \"lobby2\"")
@Since("1.1.0")
public class EffSendCustomBungeeMessage extends Effect {

    static {
        Skript.registerEffect(EffSendCustomBungeeMessage.class,
                "send custom message %string% to %bungeeservers%");
    }

    private Expression<String> message;
    private Expression<BungeeServer> servers;

    @Override
    public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.message = (Expression<String>) exprs[0];
        this.servers = (Expression<BungeeServer>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (BungeeSK.isClientConnected()) {
            for (final BungeeServer server : servers.getArray(e)) {
                ConnectionClient.get().write(true, "effectSendBungeeCustomMessage",
                        "serverAddress", server.getAddress(),
                        "serverPort", String.valueOf(server.getPort()),
                        "message", this.message.getSingle(e));
            }
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return null;
    }

}
