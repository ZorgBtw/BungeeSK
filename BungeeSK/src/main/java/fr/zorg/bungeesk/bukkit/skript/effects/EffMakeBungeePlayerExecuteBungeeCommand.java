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
import fr.zorg.bungeesk.common.packets.MakeBungeePlayerBungeeCommandPacket;
import org.bukkit.event.Event;

@Name("Make bungee player execute bungee command")
@Description("Make a player on the bungeecord execute a specific bungeecord-sided command")
@Examples("make bungee player named \"Notch\" execute bungee command \"glist\"")
@Since("2.0.0")
public class EffMakeBungeePlayerExecuteBungeeCommand extends Effect {

    static {
        Skript.registerEffect(EffMakeBungeePlayerExecuteBungeeCommand.class,
                "make %bungeeplayer% execute bungee command %string%");
    }

    private Expression<BungeePlayer> player;
    private Expression<String> command;

    @Override
    public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.player = (Expression<BungeePlayer>) exprs[0];
        this.command = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (this.player.getSingle(e) == null)
            return;

        final MakeBungeePlayerBungeeCommandPacket packet = new MakeBungeePlayerBungeeCommandPacket(this.player.getSingle(e), this.command.getSingle(e));
        PacketClient.sendPacket(packet);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "make bungee player " + this.player.toString(e, debug) + " execute bungee command " + this.command.toString(e, debug);
    }

}