package fr.zorg.bungeesk.common.packets;


public class AuthRequestPacket implements BungeeSKPacket {

    private final byte[] encryptedUuid;

    public AuthRequestPacket(byte[] encryptedUuid) {
        this.encryptedUuid = encryptedUuid;
    }

    public byte[] getEncryptedUuid() {
        return this.encryptedUuid;
    }

}