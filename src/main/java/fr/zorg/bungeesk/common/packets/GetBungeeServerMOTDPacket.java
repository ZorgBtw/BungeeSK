package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeeServer;

public class GetBungeeServerMOTDPacket implements BungeeSKPacket {

    private final BungeeServer bungeeServer;

    public GetBungeeServerMOTDPacket(BungeeServer bungeeServer) {
        this.bungeeServer = bungeeServer;
    }

    public BungeeServer getBungeeServer() {
        return this.bungeeServer;
    }

}