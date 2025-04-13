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

@Name("Bungee port of bungee server")
@Description("Get the port of a server precised in the Bungeecord config")
@Examples("broadcast bungee port of bungee server named \"lobby\"")
@Since("1.1.0")
public class ExprPortOfBungeeServer extends SimplePropertyExpression<BungeeServer, Long> {

    static {
        register(ExprPortOfBungeeServer.class,
                Long.class,
                "bungee port",
                "bungeeserver");
    }

    @Nullable
    @Override
    public Long convert(BungeeServer server) {
        return server == null ? null : (long) server.getPort();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        return;
    }

    @Override
    public Class<? extends Long> getReturnType() {
        return Long.class;
    }

    @Override
    protected String getPropertyName() {
        return "bungee server's port";
    }

}