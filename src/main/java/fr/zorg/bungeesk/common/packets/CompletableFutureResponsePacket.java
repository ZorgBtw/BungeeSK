package fr.zorg.bungeesk.common.packets;

import java.util.UUID;

public class CompletableFutureResponsePacket implements BungeeSKPacket {

    private final UUID uuid;
    private final Object response;
    private final boolean error;

    public CompletableFutureResponsePacket(UUID uuid, Object response, boolean error) {
        this.uuid = uuid;
        this.response = response;
        this.error = error;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Object getResponse() {
        return response;
    }

    public boolean isError() {
        return this.error;
    }

}