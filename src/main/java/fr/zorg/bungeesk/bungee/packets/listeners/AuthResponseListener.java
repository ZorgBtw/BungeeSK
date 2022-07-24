package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.common.packets.AuthResponsePacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

import java.util.UUID;

public class AuthResponseListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof AuthResponsePacket) {
            final AuthResponsePacket authResponsePacket = (AuthResponsePacket) packet;
            final UUID uuid = authResponsePacket.getDecryptedUuid();
            socketServer.completeChallenge(uuid);
        }
    }

}