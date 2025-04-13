package fr.zorg.bungeesk.common.packets;

import fr.zorg.bungeesk.common.entities.BungeePlayer;

public class BungeePlayerPermissionPacket implements BungeeSKPacket {

    private final BungeePlayer bungeePlayer;
    private final String permission;

    public BungeePlayerPermissionPacket(BungeePlayer bungeePlayer, String permission) {
        this.bungeePlayer = bungeePlayer;
        this.permission = permission;
    }

    public BungeePlayer getBungeePlayer() {
        return this.bungeePlayer;
    }

    public String getPermission() {
        return this.permission;
    }

}