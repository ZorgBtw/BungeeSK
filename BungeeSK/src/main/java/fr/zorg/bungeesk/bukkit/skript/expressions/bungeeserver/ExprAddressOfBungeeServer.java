package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeserver;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Bungee address of bungee server")
@Description("Get the address of a server precised in the Bungeecord config")
@Examples("broadcast bungee address of bungee server named \"lobby\"")
@Since("1.1.0")
public class ExprAddressOfBungeeServer extends SimplePropertyExpression<BungeeServer, String> {

    static {
        register(ExprAddressOfBungeeServer.class,
                String.class,
                "bungee address",
                "bungeeserver");
    }

    @Nullable
    @Override
    public String convert(BungeeServer server) {
        return server == null ? null : server.getAddress().getHostAddress();
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
        return "bungee server's address";
    }

}