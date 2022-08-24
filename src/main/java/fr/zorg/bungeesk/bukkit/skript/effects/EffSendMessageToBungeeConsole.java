package fr.zorg.bungeesk.bukkit.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.packets.PacketClient;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.KickBungeePlayerPacket;
import fr.zorg.bungeesk.common.packets.SendMessageToConsolePacket;
import org.bukkit.event.Event;

public class EffSendMessageToBungeeConsole extends Effect {

    static {
        Skript.registerEffect(EffSendMessageToBungeeConsole.class,
                "send %string% to bungee console");
    }

    private Expression<String> message;

    @Override
    public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.message = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        PacketClient.sendPacket(new SendMessageToConsolePacket(this.message.getSingle(e)));
    }

    @Override
    public String toString(Event event, boolean b) {
        return "send " + this.message.toString(event, b) + " to bungee console";
    }

}