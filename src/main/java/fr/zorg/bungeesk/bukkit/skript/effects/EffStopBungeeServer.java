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
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.StopBungeeServerPacket;
import org.bukkit.event.Event;

@Name("Stop bungee server")
@Description("Stop a bungee server remotely")
@Examples("stop bungee server named \"lobby\"")
@Since("2.0.0")
public class EffStopBungeeServer extends Effect {

    static {
        Skript.registerEffect(EffStopBungeeServer.class,
                "stop %bungeeserver%");
    }

    private Expression<BungeeServer> server;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.server = (Expression<BungeeServer>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (this.server.getSingle(e) == null)
            return;

        final StopBungeeServerPacket packet = new StopBungeeServerPacket(this.server.getSingle(e));
        PacketClient.sendPacket(packet);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "stop bungee server " + this.server.toString(e, debug);
    }

}