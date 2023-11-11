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

@Name("Name of bungee server builder")
@Description("Set the name of a bungee server builder")
@Since("2.0.0")
@Examples("set name of server builder to \"lobby2\"")
public class ExprName extends SimplePropertyExpression<BungeeServerBuilder, String> {

    static {
        register(ExprName.class,
                String.class,
                "name",
                "serverbuilder");
    }

    @Nullable
    @Override
    public String convert(final BungeeServerBuilder builder) {
        return builder.getName();
    }

    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(final Event e, final Object[] delta, final ChangeMode mode) {
        for (BungeeServerBuilder serverBuilder : getExpr().getArray(e)) {
            serverBuilder.setName((String) delta[0]);
        }
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "name";
    }

}