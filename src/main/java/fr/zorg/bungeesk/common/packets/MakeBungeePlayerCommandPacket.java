package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeePlayer;

public class MakeBungeePlayerCommandPacket implements BungeeSKPacket {

    private final BungeePlayer bungeePlayer;
    private final String command;

    public MakeBungeePlayerCommandPacket(BungeePlayer bungeePlayer, String command) {
        this.bungeePlayer = bungeePlayer;
        this.command = command;
    }

    public BungeePlayer getBungeePlayer() {
        return this.bungeePlayer;
    }

    public String getCommand() {
        return this.command;
    }

}