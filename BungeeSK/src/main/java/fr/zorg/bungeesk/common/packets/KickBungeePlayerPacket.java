package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeePlayer;

public class KickBungeePlayerPacket implements BungeeSKPacket {

    private final BungeePlayer bungeePlayer;
    private final String reason;

    public KickBungeePlayerPacket(BungeePlayer bungeePlayer, String reason) {
        this.bungeePlayer = bungeePlayer;
        this.reason = reason;
    }

    public BungeePlayer getBungeePlayer() {
        return this.bungeePlayer;
    }

    public String getReason() {
        return this.reason;
    }

}