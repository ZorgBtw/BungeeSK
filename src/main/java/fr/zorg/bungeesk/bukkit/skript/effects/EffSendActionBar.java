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
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import org.bukkit.event.Event;

@Name("Send action bar message")
@Description("Send an action bar message to a player on the bungeecord network")
@Examples("send (bungee player named \"Notch\") action bar \"&6Welcome ! :)\"")
@Since("1.1.0")
public class EffSendActionBar extends Effect {

    static {
        Skript.registerEffect(EffSendActionBar.class,
                "send %bungeeplayer% action bar [message] %string%");
    }

    private Expression<BungeePlayer> player;
    private Expression<String> message;

    @Override
    public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.player = (Expression<BungeePlayer>) exprs[0];
        this.message = (Expression<String>) exprs[1];
        return true;
    }
    @Override
    protected void execute(Event e) {
        if (BungeeSK.isClientConnected()) {

            if (this.player.getSingle(e) == null)
                 return;

            ConnectionClient.get().write(true, "effectSendActionBar",
                    "playerUuid", this.player.getSingle(e).getUuid(),
                    "message", this.message.getSingle(e));
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "send action bar message " + this.message.toString(e, debug) + " to bungee player " + this.player.toString(e, debug);
    }

}
