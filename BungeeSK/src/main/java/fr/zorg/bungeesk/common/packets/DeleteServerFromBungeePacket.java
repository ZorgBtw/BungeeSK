package fr.zorg.bungeesk.common.packets;

public class DeleteServerFromBungeePacket implements BungeeSKPacket {

    private final String serverName;

    public DeleteServerFromBungeePacket(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return this.serverName;
    }

}