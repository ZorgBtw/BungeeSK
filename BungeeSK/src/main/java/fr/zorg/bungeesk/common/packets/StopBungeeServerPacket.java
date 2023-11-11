package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeeServer;

public class StopBungeeServerPacket implements BungeeSKPacket {

    private final BungeeServer server;

    public StopBungeeServerPacket(BungeeServer server) {
        this.server = server;
    }

    public BungeeServer getServer() {
        return this.server;
    }

}