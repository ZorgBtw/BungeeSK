package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeePlayer;

public class BungeeCommandPacket implements BungeeSKPacket {

    private final String command;
    private final BungeePlayer player;

    public BungeeCommandPacket(String command, BungeePlayer player) {
        this.command = command;
        this.player = player;
    }

    public String getCommand() {
        return this.command;
    }

    public BungeePlayer getPlayer() {
        return this.player;
    }

}