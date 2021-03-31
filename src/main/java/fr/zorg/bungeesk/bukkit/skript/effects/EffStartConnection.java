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
import fr.zorg.bungeesk.bukkit.sockets.ClientSettings;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Start new connection")
@Description("Start a new connection to bungeecord")
@Examples("on load:\n" +
        "\tcreate new bungee connection:\n" +
        "\t\tset address of connection to \"127.0.0.1\"\n" +
        "\t\tset port of connection to 100\n" +
        "\t\tset password of connection to \"abcd\"\n" +
        "\t\tset name of connection to \"server1\"\n" +
        "\tstart new connection with connection")
@Since("1.0.0")

public class EffStartConnection extends Effect {

    static  {
        Skript.registerEffect(EffStartConnection.class, "start new connection with %bungeeconn%");
    }

    private Expression<ClientSettings> clientSettingsExpression;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.clientSettingsExpression = (Expression<ClientSettings>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        clientSettingsExpression.getSingle(e).run();
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "start connection with bungee connection";
    }

}
