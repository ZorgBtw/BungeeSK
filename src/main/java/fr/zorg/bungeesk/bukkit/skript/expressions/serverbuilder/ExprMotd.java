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

@Name("MOTD of bungee server builder")
@Description("Set the MOTD of a bungee server builder")
@Since("2.0.0")
@Examples("set motd of server builder to \"This is a dynamic server\"")
public class ExprMotd extends SimplePropertyExpression<BungeeServerBuilder, String> {

    static {
        register(ExprMotd.class,
                String.class,
                "motd",
                "serverbuilder");
    }

    @Nullable
    @Override
    public String convert(final BungeeServerBuilder builder) {
        return builder.getMotd();
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
        for (BungeeServerBuilder serverBuilder : getExpr().getArray(e))
            serverBuilder.setMotd((String) delta[0]);
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "motd";
    }

}