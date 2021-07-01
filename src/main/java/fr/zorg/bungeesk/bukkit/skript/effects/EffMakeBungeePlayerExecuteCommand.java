package fr.zorg.bungeesk.bukkit.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import jdk.jfr.Description;
import jdk.jfr.Name;
import org.bukkit.event.Event;

@Name("Make BungeePlayer execute command")
@Description("Make a player on the bungeecord execute a specific command")
@Examples("make bungee player named \"Zorg_btw\" execute command \"say What's up people !\"")
@Since("1.1.0")
public class EffMakeBungeePlayerExecuteCommand extends Effect {

    static {
        Skript.registerEffect(EffMakeBungeePlayerExecuteCommand.class,
                "make %bungeeplayer% execute command %string%");
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

            ConnectionClient.get().write(true, "effectMakeBungeePlayerExecuteCommand",
                    "playerUniqueId", this.player.getSingle(e).getUuid(),
                    "command", this.message.getSingle(e));
        }

    }

    @Override
    public String toString(Event e, boolean debug) {
        return "make bungee player " + this.player.toString(e, debug) + " execute command " + this.message.toString(e, debug);
    }

}
