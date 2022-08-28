package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeeServer;

public class GetRequestFromOtherServerPacket implements BungeeSKPacket {

    private final String request;
    private final BungeeServer server;

    public GetRequestFromOtherServerPacket(String request, BungeeServer server) {
        this.request = request;
        this.server = server;
    }

    public String getRequest() {
        return this.request;
    }

    public BungeeServer getServer() {
        return this.server;
    }

}