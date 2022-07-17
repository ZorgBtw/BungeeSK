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

@Name("Password of connection")
@Description("Set the password to connect to a bungeecord server.")
@Since("1.0.0")
@Examples("set password of {_connection} to \"abdc123\"")
public class ExprPassword extends SimplePropertyExpression<ClientBuilder, String> {

    static {
        register(ExprPassword.class,
                String.class,
                "password",
                "bungeeconn");
    }

    @Nullable
    @Override
    public String convert(ClientBuilder builder) {
        return builder.getAddress();
    }

    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, Object[] delta, ChangeMode mode) {
        for (final ClientBuilder builder : super.getExpr().getArray(e)) {
            final String password = (String) delta[0];
            builder.setPassword(password.toCharArray());
        }
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "password";
    }

}