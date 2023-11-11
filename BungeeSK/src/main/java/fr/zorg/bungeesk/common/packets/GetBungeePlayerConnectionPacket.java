package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeePlayer;

public class GetBungeePlayerConnectionPacket implements BungeeSKPacket {

    private final BungeePlayer bungeePlayer;

    public GetBungeePlayerConnectionPacket(BungeePlayer bungeePlayer) {
        this.bungeePlayer = bungeePlayer;
    }

    public BungeePlayer getBungeePlayer() {
        return this.bungeePlayer;
    }

}