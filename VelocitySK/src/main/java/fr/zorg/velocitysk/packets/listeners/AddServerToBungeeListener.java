package fr.zorg.velocitysk.packets.listeners;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import fr.zorg.bungeesk.common.entities.BungeeServerBuilder;
import fr.zorg.bungeesk.common.packets.AddServerToBungeePacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.velocitysk.BungeeSK;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;

import java.net.InetSocketAddress;

public class AddServerToBungeeListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof AddServerToBungeePacket) {
            final AddServerToBungeePacket addServerToBungeePacket = (AddServerToBungeePacket) packet;
            final BungeeServerBuilder builder = addServerToBungeePacket.getBuilder();
            final InetSocketAddress address = new InetSocketAddress(builder.getIp(), builder.getPort());
            final String name = builder.getName();

            final ServerInfo serverInfo = new ServerInfo(name, address);
            final RegisteredServer registeredServer = BungeeSK.getServer().createRawRegisteredServer(serverInfo);

            BungeeSK.getServer().getAllServers().add(registeredServer);
        }
    }

}