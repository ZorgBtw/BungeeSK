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
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.KickBungeePlayerPacket;
import org.bukkit.event.Event;

@Name("Kick bungee player")
@Description("Kicks a player on the network from the network")
@Examples("kick bungee player named \"Notch\" from bungeecord due to \"&cYou're the fake Notch !\"")
@Since("1.1.0")
public class EffKickBungeePlayer extends Effect {

    static {
        Skript.registerEffect(EffKickBungeePlayer.class,
                "kick %bungeeplayer% from bungee[cord] [(due to|because of) %-string%]");
    }

    private Expression<BungeePlayer> player;
    private Expression<String> reason;

    @Override
    public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.player = (Expression<BungeePlayer>) exprs[0];
        this.reason = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        final BungeePlayer bungeePlayer = this.player.getSingle(e);
        if (bungeePlayer == null)
            return;

        PacketClient.sendPacket(new KickBungeePlayerPacket(bungeePlayer, this.reason == null ? null : this.reason.getSingle(e)));
    }

    @Override
    public String toString(Event event, boolean b) {
        return "kicks bungee player with uuid " + this.player.toString(event, b);
    }

}