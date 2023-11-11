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

@Name("Ping's favicon URL")
@Description("The favicon URL displayed in a proxy ping event")
@Examples("on proxy ping:\n\tset favicon url to \"https://i.imgur.com/KE23iIh.png\"")
@Since("2.0.0")
public class ExprFaviconUrl extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprFaviconUrl.class, String.class, ExpressionType.SIMPLE, "favicon [url]");
    }

    private boolean isBungeePingEvent;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.isBungeePingEvent = super.getParser().isCurrentEvent(BungeePingEvent.class);
        if (!this.isBungeePingEvent) {
            Skript.error("The favicon url can only be used in a BungeePingEvent");
            return false;
        }
        return true;
    }

    @Override
    protected String[] get(Event e) {
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

        ((BungeePingEvent) e).getPing().setFavicon((String) delta[0]);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "favicon url in a bungee ping event";
    }
}