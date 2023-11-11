package fr.zorg.bungeesk.bukkit.skript.expressions;

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
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.variables.SerializedVariable;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.zorg.bungeesk.bukkit.skript.events.bukkit.CustomRequestEvent;
import fr.zorg.bungeesk.common.utils.Pair;
import org.bukkit.event.Event;

@Name("Custom request response")
@Description("Set the response of a custom request")
@Examples("broadcast custom request \"hello\" from bungee server named \"lobby\"\n" +
        "\n" +
        "#On the other server\n" +
        "on custom request:\n" +
        "\tif event-string is \"hello\":\n" +
        "\t\tset custom request response to \"hi !\"")
@Since("2.0.0")
public class ExprCustomRequestResponse extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprCustomRequestResponse.class, Object.class, ExpressionType.SIMPLE, "custom request response");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!super.getParser().isCurrentEvent(CustomRequestEvent.class)) {
            Skript.error("The custom request response expression can only be used in a custom request event");
            return false;
        }
        if (isDelayed.isTrue()) {
            Skript.error("Custom request response expression cannot be delayed");
            return false;
        }
        return true;
    }

    @Override
    protected Object[] get(Event e) {
        return new Object[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(Object.class);
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            if (e instanceof CustomRequestEvent && delta != null) {
                SerializedVariable.Value value = Classes.serialize(delta[0]);
                if (value == null)
                    return;
                final Pair<String, byte[]> valuePair = Pair.from(value.type, value.data);
                ((CustomRequestEvent) e).setResponse(valuePair);
            }
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "custom request response";
    }

}