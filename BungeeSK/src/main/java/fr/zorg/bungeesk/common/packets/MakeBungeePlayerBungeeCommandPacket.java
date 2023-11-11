package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeePlayer;

public class MakeBungeePlayerBungeeCommandPacket implements BungeeSKPacket {

    private final BungeePlayer player;
    private final String command;

    public MakeBungeePlayerBungeeCommandPacket(BungeePlayer player, String command) {
        this.player = player;
        this.command = command;
    }

    public BungeePlayer getPlayer() {
        return this.player;
    }

    public String getCommand() {
        return this.command;
    }

}