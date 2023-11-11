package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.BungeeServer;

public class SendBungeePlayerToServerPacket implements BungeeSKPacket {

    private final BungeePlayer bungeePlayer;
    private final BungeeServer bungeeServer;

    public SendBungeePlayerToServerPacket(BungeePlayer bungeePlayer, BungeeServer bungeeServer) {
        this.bungeePlayer = bungeePlayer;
        this.bungeeServer = bungeeServer;
    }

    public BungeePlayer getBungeePlayer() {
        return this.bungeePlayer;
    }

    public BungeeServer getBungeeServer() {
        return this.bungeeServer;
    }

}