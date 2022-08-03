package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeeServer;

public class BungeeServerStopPacket implements BungeeSKPacket {

    private final BungeeServer server;

    public BungeeServerStopPacket(BungeeServer server) {
        this.server = server;
    }

    public BungeeServer getServer() {
        return this.server;
    }

}