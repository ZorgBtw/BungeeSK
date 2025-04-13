package fr.zorg.bungeesk.common.packets;

public class BroadcastToNetworkPacket implements BungeeSKPacket {

    private final String message;

    public BroadcastToNetworkPacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}