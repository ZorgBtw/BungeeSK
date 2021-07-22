package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeplayer;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.google.gson.JsonObject;
import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.sockets.ConnectionClient;
import fr.zorg.bungeesk.bukkit.utils.BungeePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("IP address of BungeePlayer")
@Description("Gets the IP address of a BungeePlayer")
@Examples("send (bungee player named \"Zorg_btw\")'s ip")
@Since("1.1.0")
public class ExprBungeePlayerIP extends SimplePropertyExpression<BungeePlayer, String> {

    static {
        register(ExprBungeePlayerIP.class,
                String.class,
                "ip [address]",
                "bungeeplayer");
    }

    @Nullable
    @Override
    public String convert(BungeePlayer player) {
        if (BungeeSK.isClientConnected()) {
            if (player == null)
                return null;

            final JsonObject result = ConnectionClient.get().future("expressionGetBungeePlayerIP",
                    "playerUniqueId", player.getUuid());

            if (result.get("error").getAsBoolean())
                return null;

            return result.get("address").getAsString();
        }
        return null;
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
        return "bungee player's ip address";
    }

}
