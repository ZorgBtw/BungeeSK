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
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Broadcast message to server")
@Description("Broadcasts a message to a server in the network")
@Examples("broadcast \"&aHello world !\" to server \"hub\"")
@Since("1.0.3")
public class EffBroadcastMessageToServer extends Effect {

    static {
        Skript.registerEffect(EffBroadcastMessageToServer.class,
                "broadcast %string% to server %string%");
    }

    private Expression<String> message;
    private Expression<String> server;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        message = (Expression<String>) exprs[0];
        server = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        assert ConnectionClient.get() != null;
        ConnectionClient.get().write("BROADCASTTOSERVµ" + server.getSingle(e) + "µ" + message.getSingle(e));
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "broadcasts message " + message.toString(e, debug) + " to server " + server.toString(e, debug);
    }

}
