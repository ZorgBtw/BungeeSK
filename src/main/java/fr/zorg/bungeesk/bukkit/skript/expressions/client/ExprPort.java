package fr.zorg.bungeesk.bukkit.skript.expressions.client;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.zorg.bungeesk.bukkit.skript.scopes.ScopeConnectToServer;
import fr.zorg.bungeesk.bukkit.sockets.ClientSettings;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprPort extends SimplePropertyExpression<ClientSettings, Long> {

    static {
        register(ExprPort.class,
                Long.class,
                "port",
                "bungeeconn");
    }


    @Nullable
    @Override
    public Long convert(ClientSettings clientSettings) {
        return clientSettings.getPort();
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
        for (ClientSettings clientSettings : getExpr().getArray(e)) {
            clientSettings.setPort((Long) delta[0]);
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
