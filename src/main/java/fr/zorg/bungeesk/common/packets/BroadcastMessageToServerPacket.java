package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeeServer;

public class BroadcastMessageToServerPacket implements BungeeSKPacket {

    private final BungeeServer bungeeServer;
    private final String message;

    public BroadcastMessageToServerPacket(BungeeServer bungeeServer, String message) {
        this.bungeeServer = bungeeServer;
        this.message = message;
    }

    public BungeeServer getBungeeServer() {
        return this.bungeeServer;
    }

    public String getMessage() {
        return this.message;
    }

}
