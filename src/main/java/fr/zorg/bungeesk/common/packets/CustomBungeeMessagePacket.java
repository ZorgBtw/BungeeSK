package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeeServer;

public class CustomBungeeMessagePacket implements BungeeSKPacket {

    private final BungeeServer from;
    private final String message;

    public CustomBungeeMessagePacket(BungeeServer from, String message) {
        this.from = from;
        this.message = message;
    }

    public BungeeServer getFrom() {
        return this.from;
    }

    public String getMessage() {
        return this.message;
    }

}