package fr.zorg.bungeesk.common.packets;

import java.util.UUID;

public class CompletableFuturePacket implements BungeeSKPacket {

    private final BungeeSKPacket packet;
    private final UUID uuid;

    public CompletableFuturePacket(BungeeSKPacket packet, UUID uuid) {
        this.packet = packet;
        this.uuid = uuid;
    }

    public BungeeSKPacket getPacket() {
        return this.packet;
    }

    public UUID getUuid() {
        return this.uuid;
    }

}