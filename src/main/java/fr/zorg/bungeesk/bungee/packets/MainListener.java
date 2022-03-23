package fr.zorg.bungeesk.bungee.packets;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.Debug;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.HandshakePacket;

public class MainListener extends Listener {

    @Override
    public void received(Connection connection, Object packetRaw) {
        if (!(packetRaw instanceof BungeeSKPacket)) {
            Debug.log("Unknown packet received ! (" + packetRaw.getClass().getName() + ")");
            return;
        }
        Debug.log("BungeeSK packet received: (" + packetRaw.getClass().getName() + ")");

        final BungeeSKPacket packet = (BungeeSKPacket) packetRaw;

        if (!(PacketServer.getServersMap().containsKey(connection))) { //The server is not authenticated
            if (!(packet instanceof HandshakePacket)) {
                final BungeeServer bungeeServer = new BungeeServer(connection.getRemoteAddressTCP().getAddress());
                PacketServer.getServersMap().put(connection, bungeeServer);
                connection.sendTCP(new HandshakePacket());
                return;
            }
            connection.close();
            return;
        }

        final BungeeServer server = PacketServer.getServersMap().get(connection);
        BungeeSK.getApi().getListeners().forEach(bungeeListener -> {
            try {
                bungeeListener.getClass().getMethod("onReceive", BungeeServer.class, BungeeSKPacket.class); //Check if method exists
                bungeeListener.onReceive(server, packet);
            } catch (NoSuchMethodException ignored) {
            }
        });

    }

}