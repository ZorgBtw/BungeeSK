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
import org.bukkit.event.Event;

@Name("Broadcast message to the network")
@Description("Broadcast message to the network, like the /alert command does but with more personalization (the messages will not appear in the spigot consoles)")
@Examples("broadcast \"&6Hello everyone\" to network")
@Since("1.1.0")
public class EffBroadcastMessageToNetwork extends Effect {

    static {
        Skript.registerEffect(EffBroadcastMessageToNetwork.class,
                "broadcast %string% to [the] network");
    }

    private Expression<String> message;

    @Override
    public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.message = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (BungeeSK.isClientConnected()) {
            ConnectionClient.get().write(true, "effectBroadcastMessageToNetwork",
                    "message", this.message.getSingle(e));
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "broadcast message " + this.message.toString(e, debug) + " into the whole bungeecord network";
    }
}
