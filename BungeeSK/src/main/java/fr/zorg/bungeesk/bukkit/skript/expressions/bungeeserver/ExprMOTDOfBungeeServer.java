package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeserver;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import fr.zorg.bungeesk.bukkit.utils.CompletableFutureUtils;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.GetBungeeServerMOTDPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Bungee motd of bungee server")
@Description("Get the MOTD of a server precised in the Bungeecord config")
@Examples("broadcast bungee motd of bungee server named \"lobby\"")
@Since("1.1.0")
public class ExprMOTDOfBungeeServer extends SimplePropertyExpression<BungeeServer, String> {

    static {
        register(ExprMOTDOfBungeeServer.class,
                String.class,
                "bungee motd",
                "bungeeserver");
    }

    @Nullable
    @Override
    public String convert(BungeeServer server) {
        if (server == null)
            return null;

        final GetBungeeServerMOTDPacket packet = new GetBungeeServerMOTDPacket(server);
        final String response = (String) CompletableFutureUtils.generateFuture(packet);

        if (response == null)
            return null;

        return response;
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
        return "bungee server's motd";
    }
}