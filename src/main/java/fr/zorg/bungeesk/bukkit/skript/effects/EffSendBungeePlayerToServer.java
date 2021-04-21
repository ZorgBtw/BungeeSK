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
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Send bungee player to server")
@Description("Send a player on the network to a specific server")
@Examples("send bungee player \"Zorg_btw\" to server \"lobby2\"")
@Since("1.0.0")
public class EffSendBungeePlayerToServer extends Effect {

    static {
        Skript.registerEffect(EffSendBungeePlayerToServer.class,
                "send %bungeeplayer% to server %string%");
    }

    private Expression<BungeePlayer> player;
    private Expression<String> server;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<BungeePlayer>) exprs[0];
        server = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        ConnectionClient.get().write("SENDTOSERVµ" + player.getSingle(e).getData() + "^" + server.getSingle(e));
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "send bungee player " + player.toString(e, debug) + " to server " + server.toString(e, debug);
    }

}
