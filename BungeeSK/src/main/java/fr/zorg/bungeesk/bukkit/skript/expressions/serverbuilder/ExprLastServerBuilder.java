package fr.zorg.bungeesk.bukkit.skript.expressions.serverbuilder;

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
import fr.zorg.bungeesk.bukkit.skript.sections.SecCreateBungeeServer;
import fr.zorg.bungeesk.common.entities.BungeeServerBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Last created bungee server builder")
@Description("Returns the last created bungee server builder inside of a scope")
@Since("2.0.0")
@Examples("create new bungee server:\n" +
        "\tset name of server builder to \"lobby2\"\n" +
        "\tset address of server builder to \"127.0.0.1\"\n" +
        "\tset port of server builder to 25567\n" +
        "\tset motd of server builder to \"This is lobby number 2\"\n" +
        "put server into bungeecord")
public class ExprLastServerBuilder extends SimpleExpression<BungeeServerBuilder> {

    static {
        Skript.registerExpression(ExprLastServerBuilder.class, BungeeServerBuilder.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] [bungee] server [builder]");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected BungeeServerBuilder[] get(Event e) {
        return new BungeeServerBuilder[]{SecCreateBungeeServer.builder};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends BungeeServerBuilder> getReturnType() {
        return BungeeServerBuilder.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "last created bungee server builder";
    }

}