package fr.zorg.velocitysk.packets.listeners;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.SendBungeePlayerToServerPacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;

public class SendBungeePlayerToServerListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof SendBungeePlayerToServerPacket) {
            final SendBungeePlayerToServerPacket sendBungeePlayerToServerPacket = (SendBungeePlayerToServerPacket) packet;
            final Player player = VelocityUtils.getPlayer(sendBungeePlayerToServerPacket.getBungeePlayer());
            if (player == null)
                return;

            final RegisteredServer registeredServer = VelocityUtils.getRegisteredServer(sendBungeePlayerToServerPacket.getBungeeServer());
            if (registeredServer == null)
                return;

            player.createConnectionRequest(registeredServer).fireAndForget();
        }
    }

}