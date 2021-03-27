package fr.zorg.bungeesk.bukkit.skript.expressions.client;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.sockets.ClientSettings;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

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
            if (password.contains("µ")) {
                BungeeSK.getInstance().getLogger().log(Level.SEVERE, "Password of a connection must not contain the µ character");
                return;
            }
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
