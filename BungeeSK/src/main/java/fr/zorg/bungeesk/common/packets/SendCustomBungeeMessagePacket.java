package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeeServer;

import java.util.ArrayList;

public class SendCustomBungeeMessagePacket implements BungeeSKPacket {

    private final ArrayList<BungeeServer> servers;
    private final String message;

    public SendCustomBungeeMessagePacket(ArrayList<BungeeServer> servers, String message) {
        this.servers = servers;
        this.message = message;
    }

    public ArrayList<BungeeServer> getServers() {
        return this.servers;
    }

    public String getMessage() {
        return this.message;
    }

}