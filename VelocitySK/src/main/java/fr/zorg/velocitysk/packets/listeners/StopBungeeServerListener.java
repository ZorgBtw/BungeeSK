package fr.zorg.velocitysk.packets.listeners;

import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.StopBungeeServerPacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;

public class StopBungeeServerListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof StopBungeeServerPacket) {
            final StopBungeeServerPacket stopBungeeServerPacket = (StopBungeeServerPacket) packet;
            final BungeeServer server = stopBungeeServerPacket.getServer();
            final SocketServer serverSocket = VelocityUtils.getSocketFromBungeeServer(server);

            if (serverSocket != null) {
                serverSocket.sendPacket(packet);
            }
        }
    }
}