package fr.zorg.bungeesk.bukkit.skript.expressions.eventvalues;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.ServerSwitchEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprPastServer extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprPastServer.class,
                String.class,
                ExpressionType.SIMPLE,
                "past-server");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!ScriptLoader.isCurrentEvent(ServerSwitchEvent.class)) {
            Skript.error("%past-server% can only be get in a Server Switch Event !");
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    protected String[] get(Event e) {
        return new String[] { ((ServerSwitchEvent) e).getServer() };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "past server of " + ((ServerSwitchEvent) e).getPlayer().getPlayer();
    }

}
