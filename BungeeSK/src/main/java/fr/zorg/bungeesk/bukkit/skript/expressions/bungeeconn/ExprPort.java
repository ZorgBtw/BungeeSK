package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeconn;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.zorg.bungeesk.bukkit.utils.ClientBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Port of connection")
@Description("Set the port of a connection")
@Since("1.0.0")
@Examples("set port of {_connection} to 20000")
public class ExprPort extends SimplePropertyExpression<ClientBuilder, Long> {

    static {
        register(ExprPort.class,
                Long.class,
                "port",
                "bungeeconn");
    }

    @Nullable
    @Override
    public Long convert(ClientBuilder builder) {
        return (long) builder.getPort();
    }

    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET) {
            return CollectionUtils.array(Long.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, Object[] delta, ChangeMode mode) {
        for (ClientBuilder builder : getExpr().getArray(e)) {
            builder.setPort(((Long) delta[0]).intValue());
        }
    }

    @Override
    public Class<? extends Long> getReturnType() {
        return Long.class;
    }

    @Override
    protected String getPropertyName() {
        return "port";
    }
}