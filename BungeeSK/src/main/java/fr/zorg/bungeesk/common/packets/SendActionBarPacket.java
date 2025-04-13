package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeePlayer;

public class SendActionBarPacket implements BungeeSKPacket {

    private final BungeePlayer bungeePlayer;
    private final String message;

    public SendActionBarPacket(BungeePlayer bungeePlayer, String message) {
        this.bungeePlayer = bungeePlayer;
        this.message = message;
    }

    public BungeePlayer getBungeePlayer() {
        return this.bungeePlayer;
    }

    public String getMessage() {
        return this.message;
    }

}