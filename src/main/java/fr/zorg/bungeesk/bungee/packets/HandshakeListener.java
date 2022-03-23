package fr.zorg.bungeesk.bungee.packets;

import fr.zorg.bungeesk.bungee.api.BungeeListener;
import fr.zorg.bungeesk.common.entities.AuthenticationState;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.AuthRequestPacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

public class HandshakeListener extends BungeeListener {

    @Override
    public void onReceive(BungeeServer server, BungeeSKPacket packetRaw) {

        if (!(server.getState().equals(AuthenticationState.HANDSHAKE) && packetRaw instanceof AuthRequestPacket)) {
            return;
        }

        final AuthRequestPacket packet = (AuthRequestPacket) packetRaw;


    }
}
