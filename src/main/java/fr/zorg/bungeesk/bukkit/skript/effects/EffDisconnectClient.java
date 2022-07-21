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
import fr.zorg.bungeesk.bukkit.packets.PacketClient;
import org.bukkit.event.Event;

@Name("Disconnect client from Bungeecord")
@Description("Disconnect client from the Bungeecord server")
@Examples("disconnect this client")
@Since("1.1.1")
public class EffDisconnectClient extends Effect {

    static {
        Skript.registerEffect(EffDisconnectClient.class,
                "disconnect (the|this) client");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected void execute(Event e) {
        PacketClient.getClient().disconnect();
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "disconnect the actual client from the Bungeecord";
    }
}