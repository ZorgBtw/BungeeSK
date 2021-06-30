package fr.zorg.bungeesk.bukkit.skript.expressions.eventvalues;

import ch.njol.skript.ScriptLoader;
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
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.ServerSwitchEvent;
import fr.zorg.bungeesk.bukkit.utils.BungeeServer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Server switch event past server")
@Description("Gets the server where the player was from in a server switch event")
@Examples("on server swicth:\n" +
        "\tbroadcast \"The player was in the %past-server% server !\"")
@Since("1.0.0 - 1.1.0: Returns bungee server")
public class ExprPastServer extends SimpleExpression<BungeeServer> {

    static {
        Skript.registerExpression(ExprPastServer.class,
                BungeeServer.class,
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
    protected BungeeServer[] get(Event e) {
        return new BungeeServer[]{((ServerSwitchEvent) e).getServer()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends BungeeServer> getReturnType() {
        return BungeeServer.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "past server of " + ((ServerSwitchEvent) e).getPlayer().getName();
    }

}
