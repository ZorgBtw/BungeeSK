package fr.zorg.bungeesk.common.packets;

import java.util.UUID;

public class AuthResponsePacket implements BungeeSKPacket {

    private final UUID decryptedUuid;

    public AuthResponsePacket(UUID decryptedUuid) {
        this.decryptedUuid = decryptedUuid;
    }

    public UUID getDecryptedUuid() {
        return this.decryptedUuid;
    }

}