package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.KickBungeePlayerPacket;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetAddress;

public class KickBungeePlayerListener extends BungeeSKListener {

    @Override
    public void onReceive(InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof KickBungeePlayerPacket) {
            final KickBungeePlayerPacket kickBungeePlayerPacket = (KickBungeePlayerPacket) packet;
            final BungeePlayer bungeePlayer = kickBungeePlayerPacket.getBungeePlayer();
            final String reason = kickBungeePlayerPacket.getReason();
            final ProxiedPlayer proxiedPlayer = BungeeUtils.getPlayer(bungeePlayer);
            if (proxiedPlayer == null)
                return;

            if (reason == null)
                proxiedPlayer.disconnect();
            else
                proxiedPlayer.disconnect(BungeeUtils.getTextComponent(reason));
        }
    }

}