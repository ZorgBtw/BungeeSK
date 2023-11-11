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
import fr.zorg.bungeesk.common.packets.ListeningToProxyPingPacket;
import org.bukkit.event.Event;

@Name("Listen to proxy ping")
@Description("Inform the Bungeecord server that this bungee server is listening to the proxy ping event")
@Examples("on client connect:\n\tlisten to bungee proxy ping")
@Since("2.0.0")
public class EffListenToProxyPing extends Effect {

    static {
        Skript.registerEffect(EffListenToProxyPing.class,
                "listen to bungee proxy ping");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected void execute(Event e) {
        final ListeningToProxyPingPacket packet = new ListeningToProxyPingPacket();
        PacketClient.sendPacket(packet);
    }

    @Override
    public String toString(Event event, boolean b) {
        return "inform bungeecord server that this server is listening to proxy ping";
    }

}