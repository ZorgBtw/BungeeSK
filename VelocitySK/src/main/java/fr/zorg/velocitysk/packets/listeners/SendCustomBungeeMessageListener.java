package fr.zorg.velocitysk.packets.listeners;

import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.CustomBungeeMessagePacket;
import fr.zorg.bungeesk.common.packets.SendCustomBungeeMessagePacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;

public class SendCustomBungeeMessageListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof SendCustomBungeeMessagePacket) {
            final SendCustomBungeeMessagePacket sendCustomBungeeMessagePacket = (SendCustomBungeeMessagePacket) packet;
            for (BungeeServer server : sendCustomBungeeMessagePacket.getServers()) {
                final SocketServer socketServer1 = VelocityUtils.getSocketFromBungeeServer(server);
                if (socketServer1 != null)
                    socketServer1.sendPacket(new CustomBungeeMessagePacket(VelocityUtils.getServerFromSocket(socketServer), sendCustomBungeeMessagePacket.getMessage()));
            }
        }
    }

}