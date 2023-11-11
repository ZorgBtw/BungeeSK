package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeping;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.BungeePingEvent;
import org.bukkit.event.Event;

@Name("Ping's MOTD")
@Description("The MOTD displayed when a player pings the proxy")
@Examples("on proxy ping:\n\tset ping motd to \"&aLine 1%nl%&2Line 2\"")
@Since("2.0.0")
public class ExprPingMotd extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprPingMotd.class, String.class, ExpressionType.SIMPLE, "ping motd");
    }

    private boolean isBungeePingEvent;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.isBungeePingEvent = super.getParser().isCurrentEvent(BungeePingEvent.class);
        if (!this.isBungeePingEvent) {
            Skript.error("The ping motd can only be used in a BungeePingEvent");
            return false;
        }
        return true;
    }

    @Override
    protected String[] get(Event e) {
        if (e instanceof BungeePingEvent) {
            return new String[]{((BungeePingEvent) e).getPing().getMotd()};
        }
        return new String[0];
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
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(String.class);
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if ((!(e instanceof BungeePingEvent)) || mode != Changer.ChangeMode.SET)
            return;

        ((BungeePingEvent) e).getPing().setMotd((String) delta[0]);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "ping motd in a bungee ping event";
    }
}