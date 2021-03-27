package fr.zorg.bungeesk.bukkit.skript.expressions.client;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.zorg.bungeesk.bukkit.sockets.ClientSettings;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprName extends SimplePropertyExpression<ClientSettings, String> {

    static {
        register(ExprName.class,
                String.class,
                "name",
                "bungeeconn");
    }


    @Nullable
    @Override
    public String convert(ClientSettings clientsettings) {
        return clientsettings.getAddress();
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
        for (ClientSettings clientSettings : getExpr().getArray(e)) {
            clientSettings.setName((String) delta[0]);
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
