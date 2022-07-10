package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.common.packets.AuthResponsePacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

import java.net.InetAddress;
import java.util.Optional;
import java.util.UUID;

public class AuthResponseListener extends BungeeSKListener {

    @Override
    public void onReceive(InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof AuthResponsePacket) {
            final AuthResponsePacket authResponsePacket = (AuthResponsePacket) packet;
            final UUID uuid = authResponsePacket.getDecryptedUuid();
            final Optional<SocketServer> client = BungeeSK.getApi().getClientWithInetAddress(address);
            if (client.isPresent()) {
                client.get().completeChallenge(uuid);
            } else {
                BungeeSK.getApi().getClientWithInetAddress(address).ifPresent(SocketServer::disconnect);
            }
        }
    }
}
