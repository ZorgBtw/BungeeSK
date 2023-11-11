package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeeServer;

public class GetBungeeServerOnlineStatusPacket implements BungeeSKPacket {

    private final BungeeServer server;

    public GetBungeeServerOnlineStatusPacket(BungeeServer server) {
        this.server = server;
    }

    public BungeeServer getServer() {
        return this.server;
    }

}