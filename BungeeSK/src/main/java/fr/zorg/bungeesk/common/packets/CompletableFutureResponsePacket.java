package fr.zorg.bungeesk.common.packets;

import java.util.UUID;

public class CompletableFutureResponsePacket implements BungeeSKPacket {

    private final UUID uuid;
    private final Object response;

    public CompletableFutureResponsePacket(UUID uuid, Object response) {
        this.uuid = uuid;
        this.response = response;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Object getResponse() {
        return response;
    }

}