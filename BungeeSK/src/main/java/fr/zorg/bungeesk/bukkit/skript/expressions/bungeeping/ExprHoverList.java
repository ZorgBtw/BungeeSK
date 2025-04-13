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

import java.util.ArrayList;

@Name("Ping's hover list")
@Description("The hover list displayed when hovering the player's size in the ping's list.")
@Examples("on proxy ping:\n\tset hover list to \"&bLine 1\", \"&3Line 2\" and \"&9Line 3\"")
@Since("2.0.0")
public class ExprHoverList extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprHoverList.class, String.class, ExpressionType.SIMPLE, "hover list");
    }

    private boolean isBungeePingEvent;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.isBungeePingEvent = super.getParser().isCurrentEvent(BungeePingEvent.class);
        if (!this.isBungeePingEvent) {
            Skript.error("The hover list can only be used in a BungeePingEvent");
            return false;
        }
        return true;
    }

    @Override
    protected String[] get(Event e) {
        if (e instanceof BungeePingEvent) {
            return ((BungeePingEvent) e).getPing().getHoverMessages().toArray(new String[0]);
        }
        return new String[0];
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
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(String[].class);
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if ((!(e instanceof BungeePingEvent)) || mode != Changer.ChangeMode.SET)
            return;

        final ArrayList<String> hoverList = new ArrayList<>();
        for (Object o : delta) {
            hoverList.add((String) o);
        }
        ((BungeePingEvent) e).getPing().setHoverMessages(hoverList);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "hover list in a bungee ping event";
    }
}