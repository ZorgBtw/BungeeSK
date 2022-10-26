package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeeServer;

public class GetRequestFromOtherServerPacket implements BungeeSKPacket {

    private final String request;
    private final BungeeServer server;
    private BungeeServer from;

    public GetRequestFromOtherServerPacket(String request, BungeeServer server, BungeeServer from) {
        this.request = request;
        this.server = server;
        this.from = from;
    }

    public String getRequest() {
        return this.request;
    }

    public BungeeServer getServer() {
        return this.server;
    }

    public BungeeServer getFrom() {
        return this.from;
    }

    public GetRequestFromOtherServerPacket setFrom(BungeeServer from) {
        this.from = from;
        return this;
    }

}