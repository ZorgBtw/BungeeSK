package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.packets.BroadcastMessagePacket;
import fr.zorg.bungeesk.common.packets.BroadcastMessageToServerPacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

public class BroadcastMessageToServerListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof BroadcastMessageToServerPacket) {
            final BroadcastMessageToServerPacket broadcastMessageToServerPacket = (BroadcastMessageToServerPacket) packet;
            final SocketServer server = BungeeUtils.getSocketFromBungeeServer(broadcastMessageToServerPacket.getBungeeServer());
            if (server != null)
                server.sendPacket(new BroadcastMessagePacket(broadcastMessageToServerPacket.getMessage()));
        }
    }
}