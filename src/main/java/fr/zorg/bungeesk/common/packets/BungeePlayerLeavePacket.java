package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeePlayer;

public class BungeePlayerLeavePacket implements BungeeSKPacket {

    private final BungeePlayer bungeePlayer;

    public BungeePlayerLeavePacket(BungeePlayer bungeePlayer) {
        this.bungeePlayer = bungeePlayer;
    }

    public BungeePlayer getBungeePlayer() {
        return this.bungeePlayer;
    }

}