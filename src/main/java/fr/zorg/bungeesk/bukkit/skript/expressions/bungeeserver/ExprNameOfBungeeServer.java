package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeserver;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import fr.zorg.bungeesk.bukkit.utils.BungeeServer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Bungee name of bungee server")
@Description("Get the name of a server precised in the Bungeecord config")
@Examples("broadcast bungee name of bungee server \"lobby\"")
@Since("1.1.0")
public class ExprNameOfBungeeServer extends SimplePropertyExpression<BungeeServer, String> {

    static {
        register(ExprNameOfBungeeServer.class,
                String.class,
                "bungee name",
                "bungeeserver");
    }

    @Nullable
    @Override
    public String convert(BungeeServer server) {
        return server == null ? null : server.getName();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        return;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "bungee server's name";
    }

}
