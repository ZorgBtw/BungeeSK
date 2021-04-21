package fr.zorg.bungeesk.bukkit.skript.expressions.client;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.zorg.bungeesk.bukkit.sockets.ClientSettings;
import org.bukkit.event.Event;
import ch.njol.skript.classes.Changer.ChangeMode;
import org.jetbrains.annotations.Nullable;

@Name("Address of connection")
@Description("Set the address of a connection")
@Since("1.0.0")
@Examples("set address of {_connection} to \"127.0.0.1\"")
public class ExprAddress extends SimplePropertyExpression<ClientSettings, String> {

    static {
        register(ExprAddress.class,
                String.class,
                "(ip|address)",
                "bungeeconn");    }

    @Nullable
    @Override
    public String convert(final ClientSettings clientSettings) {
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
    public void change(final Event e, final Object[] delta, final ChangeMode mode) {
        for (ClientSettings clientSettings : getExpr().getArray(e)) {
            clientSettings.setAddress((String) delta[0]);
        }
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "address";
    }
}
