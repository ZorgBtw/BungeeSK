package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

public class GetBungeePlayerIpPacket implements BungeeSKPacket {

    private final BungeePlayer bungeePlayer;

    public GetBungeePlayerIpPacket(BungeePlayer bungeePlayer) {
        this.bungeePlayer = bungeePlayer;
    }

    public BungeePlayer getBungeePlayer() {
        return this.bungeePlayer;
    }

}