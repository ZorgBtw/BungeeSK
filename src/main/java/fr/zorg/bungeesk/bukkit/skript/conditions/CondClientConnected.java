package fr.zorg.bungeesk.bukkit.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.packets.PacketClient;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Is bungeecord connected")
@Description("Checks if the server and the bungeecord are linked")
@Since("1.0.0")
@Examples("command /isconnected:\n" +
        "\ttrigger:\n" +
        "\t\tif client is not connected:\n" +
        "\t\t\tsend \"Not connected !\"\n" +
        "\t\tif client is connected:\n" +
        "\t\t\tsend \"Connected as well !\"")
public class CondClientConnected extends Condition {

    static {
        Skript.registerCondition(CondClientConnected.class,
                "client is connected",
                "client is(n't| not) connected");
    }

    private boolean invert;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        invert = (matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (invert)
            return !PacketClient.isConnected();
        return PacketClient.isConnected();
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "state of bungee connection";
    }
}