package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.StopBungeeServerPacket;

public class StopBungeeServerListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof StopBungeeServerPacket) {
            final StopBungeeServerPacket stopBungeeServerPacket = (StopBungeeServerPacket) packet;
            final BungeeServer server = stopBungeeServerPacket.getServer();
            final SocketServer serverSocket = BungeeUtils.getSocketFromBungeeServer(server);

            if (serverSocket != null) {
                serverSocket.sendPacket(packet);
            }
        }
    }
}