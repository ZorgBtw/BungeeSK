package fr.zorg.bungeesk.bukkit.skript.expressions.serverbuilder;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.zorg.bungeesk.common.entities.BungeeServerBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Port of bungee server builder")
@Description("Set the port of a bungee server builder")
@Since("2.0.0")
@Examples("set port of server builder to 25567")
public class ExprPort extends SimplePropertyExpression<BungeeServerBuilder, Integer> {

    static {
        register(ExprPort.class,
                Integer.class,
                "port",
                "serverbuilder");
    }

    @Nullable
    @Override
    public Integer convert(BungeeServerBuilder builder) {
        return builder.getPort();
    }

    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET) {
            return CollectionUtils.array(Integer.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, Object[] delta, ChangeMode mode) {
        for (BungeeServerBuilder builder : getExpr().getArray(e)) {
            builder.setPort((Integer) delta[0]);
        }
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    protected String getPropertyName() {
        return "port";
    }
}