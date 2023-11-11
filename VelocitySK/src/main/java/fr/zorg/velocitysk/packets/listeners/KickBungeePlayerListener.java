package fr.zorg.velocitysk.packets.listeners;

import com.velocitypowered.api.proxy.Player;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.KickBungeePlayerPacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;
import net.kyori.adventure.text.Component;

public class KickBungeePlayerListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof KickBungeePlayerPacket) {
            final KickBungeePlayerPacket kickBungeePlayerPacket = (KickBungeePlayerPacket) packet;
            final BungeePlayer bungeePlayer = kickBungeePlayerPacket.getBungeePlayer();
            final String reason = kickBungeePlayerPacket.getReason();
            final Player player = VelocityUtils.getPlayer(bungeePlayer);
            if (player == null)
                return;

            if (reason == null)
                player.disconnect(Component.empty());
            else
                player.disconnect(VelocityUtils.getTextComponent(reason));
        }
    }

}