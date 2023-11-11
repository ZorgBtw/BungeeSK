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
import fr.zorg.bungeesk.common.packets.MakeBungeePlayerCommandPacket;
import org.bukkit.event.Event;

@Name("Make bungee player execute command")
@Description("Make a player on the bungeecord execute a specific command")
@Examples("make bungee player named \"Notch\" execute command \"say What's up people !\"")
@Since("1.1.0")
public class EffMakeBungeePlayerExecuteCommand extends Effect {

    static {
        Skript.registerEffect(EffMakeBungeePlayerExecuteCommand.class,
                "make %bungeeplayer% execute command %string%");
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

        final MakeBungeePlayerCommandPacket packet = new MakeBungeePlayerCommandPacket(this.player.getSingle(e), this.command.getSingle(e));
        PacketClient.sendPacket(packet);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "make bungee player " + this.player.toString(e, debug) + " execute command " + this.command.toString(e, debug);
    }

}