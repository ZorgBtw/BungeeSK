package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.packets.BroadcastMessagePacket;
import fr.zorg.bungeesk.common.packets.BroadcastMessageToServerPacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

import java.net.InetAddress;

public class BroadcastMessageToServerListener extends BungeeSKListener {

    @Override
    public void onReceive(InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof BroadcastMessageToServerPacket) {
            final BroadcastMessageToServerPacket broadcastMessageToServerPacket = (BroadcastMessageToServerPacket) packet;
            final SocketServer socketServer = BungeeUtils.getSocketFromBungeeServer(broadcastMessageToServerPacket.getBungeeServer());
            if (socketServer != null)
                socketServer.send(new BroadcastMessagePacket(broadcastMessageToServerPacket.getMessage()));
        }
    }
}