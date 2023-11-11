package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeeServer;

public class GetAllPlayersOnServerPacket implements BungeeSKPacket {

    private final BungeeServer bungeeServer;

    public GetAllPlayersOnServerPacket(BungeeServer bungeeServer) {
        this.bungeeServer = bungeeServer;
    }

    public BungeeServer getBungeeServer() {
        return this.bungeeServer;
    }

}