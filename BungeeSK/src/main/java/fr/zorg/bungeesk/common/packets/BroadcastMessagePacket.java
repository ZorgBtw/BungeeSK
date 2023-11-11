package fr.zorg.bungeesk.common.packets;

public class BroadcastMessagePacket implements BungeeSKPacket {

    private final String message;

    public BroadcastMessagePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}