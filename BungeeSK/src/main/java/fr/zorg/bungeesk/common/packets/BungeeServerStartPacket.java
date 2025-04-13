package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeeServer;

public class BungeeServerStartPacket implements BungeeSKPacket {

    private final BungeeServer server;

    public BungeeServerStartPacket(BungeeServer server) {
        this.server = server;
    }

    public BungeeServer getServer() {
        return this.server;
    }

}