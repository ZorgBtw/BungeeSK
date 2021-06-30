package fr.zorg.bungeesk.bukkit.skript.expressions.client;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.zorg.bungeesk.bukkit.sockets.ClientSettings;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Password of connection")
@Description("Set the password to connect to a bungeecord server." +
        "NOTE: The password must not contain the 'µ' character")
@Since("1.0.0")
@Examples("set password of {_connection} to \"abdc123\"")
public class ExprPassword extends SimplePropertyExpression<ClientSettings, String> {

    static {
        register(ExprPassword.class,
                String.class,
                "password",
                "bungeeconn");
    }


    @Nullable
    @Override
    public String convert(ClientSettings clientSettings) {
        return clientSettings.getAddress();
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
        for (final ClientSettings clientSettings : getExpr().getArray(e)) {
            final String password = (String) delta[0];
            clientSettings.setPassword(password);
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
