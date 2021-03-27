package fr.zorg.bungeesk.bukkit.skript.expressions.client;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.skript.scopes.ScopeConnectToServer;
import fr.zorg.bungeesk.bukkit.sockets.ClientSettings;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprLastConnection extends SimpleExpression<ClientSettings> {

    static {
        Skript.registerExpression(ExprLastConnection.class, ClientSettings.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] (connection|server)");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected ClientSettings[] get(Event e) {
        return new ClientSettings[]{ScopeConnectToServer.settings};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ClientSettings> getReturnType() {
        return ClientSettings.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "last created connection";
    }

}
