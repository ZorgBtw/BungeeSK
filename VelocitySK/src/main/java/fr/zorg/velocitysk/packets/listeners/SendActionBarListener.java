package fr.zorg.velocitysk.packets.listeners;

import com.velocitypowered.api.proxy.Player;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.SendActionBarPacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;

public class SendActionBarListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof SendActionBarPacket) {
            final SendActionBarPacket sendActionBarPacket = (SendActionBarPacket) packet;
            final BungeePlayer bungeePlayer = sendActionBarPacket.getBungeePlayer();
            final String message = sendActionBarPacket.getMessage();
            final Player proxiedPlayer = VelocityUtils.getPlayer(bungeePlayer);
            if (proxiedPlayer == null)
                return;
            proxiedPlayer.sendActionBar(VelocityUtils.getTextComponent(message));
        }
    }

}