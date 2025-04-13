package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeconn;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.skript.sections.SecConnectToServer;
import fr.zorg.bungeesk.bukkit.utils.ClientBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Last created connection")
@Description("Returns the last created connection inside of a scope")
@Since("1.0.0")
@Examples("on load:\n" +
        "\tcreate new bungee connection:\n" +
        "\t\tset address of connection to \"127.0.0.1\"\n" +
        "\t\tset port of connection to 20000\n" +
        "\t\tset password of connection to \"abcd\"\n" +
        "\tstart new connection with connection")
public class ExprLastConnection extends SimpleExpression<ClientBuilder> {

    static {
        Skript.registerExpression(ExprLastConnection.class, ClientBuilder.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] (connection|server)");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected ClientBuilder[] get(Event e) {
        return new ClientBuilder[]{SecConnectToServer.builder};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ClientBuilder> getReturnType() {
        return ClientBuilder.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "last created connection";
    }

}