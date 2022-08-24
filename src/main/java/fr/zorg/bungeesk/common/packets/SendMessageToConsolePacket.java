package fr.zorg.bungeesk.common.packets;

public class SendMessageToConsolePacket implements BungeeSKPacket {

    private final String message;

    public SendMessageToConsolePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}