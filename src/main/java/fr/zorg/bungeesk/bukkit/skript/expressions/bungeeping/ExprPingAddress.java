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

@Name("Ping's address")
@Description("Returns the IP address of the player pinging the proxy")
@Examples("on proxy ping:\n\tset {_ip} to pinger's bungee ip")
@Since("2.0.0")
public class ExprPingAddress extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprPingAddress.class, String.class, ExpressionType.SIMPLE, "bungee ip of pinger", "pinger's bungee ip [address]");
    }

    private boolean isBungeePingEvent;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.isBungeePingEvent = super.getParser().isCurrentEvent(BungeePingEvent.class);
        if (!this.isBungeePingEvent) {
            Skript.error("Pinger's IP address can only be used in a BungeePingEvent");
            return false;
        }
        return true;
    }

    @Override
    protected String[] get(Event e) {
        if (e instanceof BungeePingEvent) {
            return new String[]{((BungeePingEvent) e).getAddress().getHostAddress()};
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
    public String toString(Event e, boolean debug) {
        return "bungee ip of pinger in a bungee player ping event";
    }
}