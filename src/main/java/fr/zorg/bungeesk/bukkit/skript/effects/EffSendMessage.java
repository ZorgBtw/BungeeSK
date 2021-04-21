package fr.zorg.bungeesk.bukkit.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.jetbrains.annotations.Nullable;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import org.bukkit.event.Event;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Effect;

@Name("Send message to bungee player")
@Description("Send a message to a bungee player on the network")
@Examples("send bungee message \"&6Hello !\" to bungee player \"Zorg_btw\"")
@Since("1.0.0")
public class EffSendMessage extends Effect {

    static {
        Skript.registerEffect(EffSendMessage.class, "send bungee message %string% to %bungeeplayer%");
    }

    private Expression<BungeePlayer> player;
    private Expression<String> message;

    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parseResult) {
        message = (Expression<String>) exprs[0];
        player = (Expression<BungeePlayer>) exprs[1];
        return true;
    }

    protected void execute(final Event e) {
        assert ConnectionClient.get() != null;
        ConnectionClient.get().write("SENDPLAYERMESSAGEµ" + player.getSingle(e).getData() + "^" + message.getSingle(e));
    }

    public String toString(@Nullable Event e, boolean debug) {
        return "send message to a bungee player";
    }

}
