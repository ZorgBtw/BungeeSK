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
import fr.zorg.bungeesk.common.packets.BroadcastMessageToServerPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Broadcast message to server")
@Description("Broadcasts a message to a server in the network")
@Examples("broadcast bungee message \"&aHello world !\" to bungee server named \"hub\"")
@Since("1.0.3 - 1.1.0: Usage of BungeeServer type")
public class EffBroadcastMessageToServer extends Effect {

    static {
        Skript.registerEffect(EffBroadcastMessageToServer.class,
                "broadcast bungee message %string% to %bungeeserver%");
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
        if (this.server.getSingle(e) == null)
            return;

        final BroadcastMessageToServerPacket packet = new BroadcastMessageToServerPacket(this.server.getSingle(e), this.message.getSingle(e));
        PacketClient.sendPacket(packet);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "broadcasts message " + this.message.toString(e, debug) + " to server " + this.server.toString(e, debug);
    }

}