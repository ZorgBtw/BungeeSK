package fr.zorg.bungeesk.bukkit.skript.expressions;

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
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("All of the bungee servers")
@Description("Returns every server connected")
@Examples("loop all bungee servers:\n" +
        "\tsend \"%loop-value%\"")
@Since("1.0.0")
public class ExprAllBungeeServers extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprAllBungeeServers.class, String.class, ExpressionType.SIMPLE,
                "[(all [[of] the]|the)] [bungee] servers");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected @Nullable String[] get(Event e) {
        assert ConnectionClient.get() != null;
        final String result = ConnectionClient.get().future("ALLBUNGEESERVERSµ");
        return result.split("\\^");
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "all bungee servers";
    }

}
