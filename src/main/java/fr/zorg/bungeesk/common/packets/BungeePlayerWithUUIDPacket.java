package fr.zorg.bungeesk.common.packets;

import java.util.UUID;

public class BungeePlayerWithUUIDPacket implements BungeeSKPacket {

    private final UUID uuid;

    public BungeePlayerWithUUIDPacket(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }

}