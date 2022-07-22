package fr.zorg.bungeesk.bukkit.skript.expressions.bungeeplayer;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import fr.zorg.bungeesk.bukkit.utils.CompletableFutureUtils;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.GetBungeePlayerIpPacket;
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
        if (player == null)
            return null;
        final GetBungeePlayerIpPacket packet = new GetBungeePlayerIpPacket(player);
        final String address = (String) CompletableFutureUtils.generateFuture(packet);
        return address;
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