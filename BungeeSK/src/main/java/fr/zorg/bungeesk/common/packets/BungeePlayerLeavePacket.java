package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.BungeeServer;

public class BungeePlayerLeavePacket implements BungeeSKPacket {

    private final BungeePlayer bungeePlayer;
    private final BungeeServer disconnectedFrom;

    public BungeePlayerLeavePacket(BungeePlayer bungeePlayer, BungeeServer disconnectedFrom) {
        this.bungeePlayer = bungeePlayer;
        this.disconnectedFrom = disconnectedFrom;
    }

    public BungeePlayer getBungeePlayer() {
        return this.bungeePlayer;
    }

    public BungeeServer getDisconnectedFrom() {
        return this.disconnectedFrom;
    }

}